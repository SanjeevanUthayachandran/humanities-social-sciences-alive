import { BookOpenCheck, CalendarClock, PenTool, UserCheck } from "lucide-react";
import { useEffect, useState } from "react";
import Container from "@/components/common/Container";
import { CourseForm, PasswordForm, ProgramForm } from "@/components/management/ManagementForms";
import { Button } from "@/components/ui/button";
import CourseRoster from "@/components/management/CourseRoster";
import { fetchCourseCategories, fetchInstructorCourses, publishCourse, type CourseCategory, type ManagementCourse } from "@/services/managementApi";
import { getRequestErrorMessage } from "@/services/requestError";

export default function InstructorDashboardPage() {
  const [categories, setCategories] = useState<CourseCategory[]>([]);
  const [courses, setCourses] = useState<ManagementCourse[]>([]);
  const [feedback, setFeedback] = useState<string | null>(null);
  useEffect(() => {
    let active = true;
    Promise.allSettled([fetchCourseCategories(), fetchInstructorCourses()]).then(([categoryResult, courseResult]) => {
      if (!active) return;
      if (categoryResult.status === "fulfilled") setCategories(categoryResult.value);
      if (courseResult.status === "fulfilled") setCourses(courseResult.value);
      const failed = [categoryResult, courseResult].find((result) => result.status === "rejected");
      if (failed?.status === "rejected") setFeedback(getRequestErrorMessage(failed.reason, "Some instructor data could not be loaded."));
    });
    return () => { active = false; };
  }, []);

  async function publish(course: ManagementCourse) {
    try { await publishCourse(course.id, true); setCourses((items) => items.map((item) => item.id === course.id ? { ...item, status: "PUBLISHED" } : item)); }
    catch (error) { setFeedback(getRequestErrorMessage(error, "The course could not be published.")); }
  }

  return <div className="min-h-screen bg-slate-50">
    <section className="bg-gradient-to-br from-slate-950 via-blue-950 to-slate-900 py-14 text-white"><Container><p className="text-sm font-bold uppercase tracking-[0.2em] text-emerald-300">Instructor studio</p><h1 className="mt-4 text-4xl font-bold tracking-tight sm:text-5xl">Create, teach, and improve your assigned learning paths.</h1><p className="mt-4 max-w-3xl text-lg leading-8 text-slate-300">Only courses connected to your registered email are shown here. Administrators control instructor assignments and payment policy.</p></Container></section>
    <Container className="py-10">{feedback ? <p className="mb-6 rounded-xl bg-red-50 p-4 text-sm font-semibold text-red-700">{feedback}</p> : null}
      <div className="grid gap-4 sm:grid-cols-3">{[[BookOpenCheck, "Assigned courses", courses.length], [PenTool, "Drafts", courses.filter((item) => item.status !== "PUBLISHED").length], [UserCheck, "Access", "Email verified"]].map(([Icon, label, value]) => { const StatIcon = Icon as typeof BookOpenCheck; return <article key={String(label)} className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm"><StatIcon className="size-5 text-blue-600" /><p className="mt-4 text-2xl font-bold text-slate-950">{String(value)}</p><p className="mt-1 text-sm font-semibold text-slate-600">{String(label)}</p></article>; })}</div>
      <section className="mt-10"><div className="flex items-end justify-between gap-4"><div><p className="text-sm font-bold uppercase tracking-[0.16em] text-blue-700">Your courses</p><h2 className="mt-2 text-2xl font-bold text-slate-950">Teaching workspace</h2></div><CalendarClock className="size-7 text-slate-400" /></div><div className="mt-5 grid gap-4 lg:grid-cols-2">{courses.map((course) => <article key={course.id} className="overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-sm">{course.cardImageUrl ? <img src={course.cardImageUrl} alt="" className="aspect-[16/7] w-full object-cover" /> : null}<div className="p-5"><div className="flex items-center justify-between gap-4"><span className="text-xs font-bold uppercase tracking-wider text-blue-700">{course.status}</span><span className="text-xs text-slate-500">{course.level.replaceAll("_", " ")}</span></div><h3 className="mt-3 text-xl font-bold text-slate-950">{course.title}</h3><p className="mt-2 text-sm text-slate-600">{course.subtitle}</p>{course.status !== "PUBLISHED" ? <Button type="button" size="sm" onClick={() => publish(course)} className="mt-5 bg-blue-600 hover:bg-blue-700">Publish course</Button> : null}</div></article>)}{courses.length === 0 ? <p className="rounded-2xl border border-dashed border-slate-300 bg-white p-6 text-sm text-slate-600">No courses are assigned to this email yet. Ask an administrator to grant access, or create a new course below.</p> : null}</div></section>
      <div className="mt-10"><CourseRoster courses={courses} /></div>
      <div className="mt-10 grid items-start gap-6 xl:grid-cols-2"><CourseForm categories={categories} onCreated={(course) => setCourses((items) => [course, ...items])} /><ProgramForm /></div><div className="mt-6"><PasswordForm /></div>
    </Container>
  </div>;
}
