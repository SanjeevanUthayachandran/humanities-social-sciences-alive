import { BookOpenCheck, CalendarCheck, UserRound, Wrench } from "lucide-react";
import { useEffect, useState, type FormEvent } from "react";
import { Link } from "react-router-dom";
import Container from "@/components/common/Container";
import { PasswordForm } from "@/components/management/ManagementForms";
import { Button } from "@/components/ui/button";
import { buttonVariants } from "@/components/ui/buttonVariants";
import { fetchMyCourses, fetchMyEvents, fetchMyWorkshops, fetchProfile, updateProfile, type ProgramRegistration, type StudentCourse, type UserProfile } from "@/services/accountApi";
import { getRequestErrorMessage } from "@/services/requestError";

const fieldClass = "mt-2 h-11 w-full rounded-lg border border-slate-200 bg-white px-3 text-sm outline-none transition focus:border-blue-500 focus:ring-3 focus:ring-blue-100";

export default function AccountPage() {
  const [profile, setProfile] = useState<UserProfile | null>(null);
  const [courses, setCourses] = useState<StudentCourse[]>([]);
  const [events, setEvents] = useState<ProgramRegistration[]>([]);
  const [workshops, setWorkshops] = useState<ProgramRegistration[]>([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [feedback, setFeedback] = useState<{ type: "success" | "error"; text: string } | null>(null);

  useEffect(() => {
    let active = true;
    fetchProfile().then(async (profileData) => {
      if (!active) return;
      setProfile(profileData);
      const results = await Promise.allSettled([fetchMyCourses(profileData.id), fetchMyEvents(), fetchMyWorkshops()]);
      if (!active) return;
      if (results[0].status === "fulfilled") setCourses(results[0].value);
      if (results[1].status === "fulfilled") setEvents(results[1].value);
      if (results[2].status === "fulfilled") setWorkshops(results[2].value);
      if (results.some((result) => result.status === "rejected")) setFeedback({ type: "error", text: "Your profile loaded, but some activity records are temporarily unavailable." });
    }).catch((error) => { if (active) setFeedback({ type: "error", text: getRequestErrorMessage(error, "Your account could not be loaded.") }); })
      .finally(() => { if (active) setLoading(false); });
    return () => { active = false; };
  }, []);

  async function saveProfile(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const form = new FormData(event.currentTarget);
    setSaving(true); setFeedback(null);
    try {
      const updated = await updateProfile({ firstName: String(form.get("firstName")), lastName: String(form.get("lastName")), displayName: String(form.get("displayName")), phoneNumber: String(form.get("phoneNumber")), title: String(form.get("title")), biography: String(form.get("biography")) });
      setProfile(updated);
      setFeedback({ type: "success", text: "Your account information has been updated." });
    } catch (error) { setFeedback({ type: "error", text: getRequestErrorMessage(error, "Your profile could not be updated.") }); }
    finally { setSaving(false); }
  }

  if (loading) return <Container className="py-20"><p className="text-slate-600">Loading your account…</p></Container>;

  const programs = [...events.map((item) => ({ ...item, label: item.eventTitle ?? "Event", path: "/events" })), ...workshops.map((item) => ({ ...item, label: item.workshopTitle ?? "Workshop", path: "/workshops" }))].sort((a, b) => String(b.registeredAt).localeCompare(String(a.registeredAt)));

  return <div className="min-h-screen bg-slate-50">
    <section className="bg-slate-950 py-12 text-white"><Container><p className="text-sm font-bold uppercase tracking-[0.2em] text-blue-300">My HSSA account</p><h1 className="mt-3 text-4xl font-bold">Welcome, {profile?.firstName ?? "learner"}.</h1><p className="mt-3 max-w-2xl text-slate-300">Manage your information and review all learning and event activity connected to your email.</p></Container></section>
    <Container className="py-10">
      {feedback ? <p role="status" className={`mb-6 rounded-xl p-4 text-sm font-semibold ${feedback.type === "success" ? "bg-emerald-50 text-emerald-800" : "bg-red-50 text-red-700"}`}>{feedback.text}</p> : null}
      <div className="grid gap-4 sm:grid-cols-3">{[[BookOpenCheck, "Courses", courses.length], [CalendarCheck, "Event registrations", events.length], [Wrench, "Workshop registrations", workshops.length]].map(([Icon, label, value]) => { const StatIcon = Icon as typeof BookOpenCheck; return <article key={String(label)} className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm"><StatIcon className="size-5 text-blue-600" /><p className="mt-4 text-3xl font-bold text-slate-950">{String(value)}</p><p className="mt-1 text-sm font-semibold text-slate-600">{String(label)}</p></article>; })}</div>

      <div className="mt-8 grid items-start gap-6 xl:grid-cols-[1.05fr_0.95fr]">
        {profile ? <form onSubmit={saveProfile} className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm"><div className="flex items-center gap-3"><span className="rounded-xl bg-blue-50 p-2.5 text-blue-700"><UserRound className="size-5" /></span><div><h2 className="font-bold text-slate-950">Personal information</h2><p className="text-sm text-slate-500">Account email: {profile.email}</p></div></div><div className="mt-6 grid gap-4 sm:grid-cols-2"><label><span className="text-sm font-semibold text-slate-700">First name</span><input name="firstName" defaultValue={profile.firstName} required className={fieldClass} /></label><label><span className="text-sm font-semibold text-slate-700">Last name</span><input name="lastName" defaultValue={profile.lastName} required className={fieldClass} /></label><label><span className="text-sm font-semibold text-slate-700">Display name</span><input name="displayName" defaultValue={profile.displayName ?? ""} className={fieldClass} /></label><label><span className="text-sm font-semibold text-slate-700">Phone</span><input name="phoneNumber" defaultValue={profile.phoneNumber ?? ""} className={fieldClass} /></label><label className="sm:col-span-2"><span className="text-sm font-semibold text-slate-700">Title or role</span><input name="title" defaultValue={profile.title ?? ""} className={fieldClass} /></label><label className="sm:col-span-2"><span className="text-sm font-semibold text-slate-700">About you</span><textarea name="biography" defaultValue={profile.biography ?? ""} className={`${fieldClass} min-h-28 py-3`} /></label></div><Button disabled={saving} className="mt-5 bg-blue-600 hover:bg-blue-700">{saving ? "Saving…" : "Save information"}</Button></form> : null}
        <div className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm"><h2 className="font-bold text-slate-950">Learning summary</h2><p className="mt-1 text-sm text-slate-500">Courses you can access with this account.</p><div className="mt-5 space-y-3">{courses.map((course) => <article key={course.enrollmentId} className="rounded-xl border border-slate-200 p-4"><div className="flex items-start justify-between gap-3"><div><h3 className="font-semibold text-slate-950">{course.courseTitle}</h3><p className="mt-1 text-xs text-slate-500">Enrolled {new Date(course.enrolledAt).toLocaleDateString()}</p></div><span className="rounded-full bg-blue-50 px-2.5 py-1 text-xs font-bold text-blue-700">{course.status.replaceAll("_", " ")}</span></div><Link to={`/courses/${course.courseSlug}`} className={buttonVariants({ variant: "outline", size: "sm", className: "mt-3" })}>Open course</Link></article>)}{courses.length === 0 ? <p className="rounded-xl bg-slate-50 p-4 text-sm text-slate-500">You have not enrolled in a course yet.</p> : null}</div></div>
      </div>

      <section className="mt-6 rounded-2xl border border-slate-200 bg-white p-6 shadow-sm"><h2 className="font-bold text-slate-950">Event and workshop activity</h2><p className="mt-1 text-sm text-slate-500">Registrations made with your HSSA account.</p><div className="mt-5 grid gap-3 md:grid-cols-2">{programs.map((item) => <article key={item.id} className="rounded-xl border border-slate-200 p-4"><div className="flex items-start justify-between gap-3"><div><h3 className="font-semibold text-slate-950">{item.label}</h3><p className="mt-1 text-xs text-slate-500">{item.startTime ? new Date(item.startTime).toLocaleString() : `Registered ${new Date(item.registeredAt).toLocaleDateString()}`}</p></div><span className="rounded-full bg-emerald-50 px-2.5 py-1 text-xs font-bold text-emerald-700">{item.status}</span></div><Link to={item.path} className="mt-3 inline-block text-sm font-semibold text-blue-700">View program</Link></article>)}{programs.length === 0 ? <p className="rounded-xl bg-slate-50 p-4 text-sm text-slate-500">No event or workshop registrations yet.</p> : null}</div></section>
      <div className="mt-6"><PasswordForm /></div>
    </Container>
  </div>;
}
