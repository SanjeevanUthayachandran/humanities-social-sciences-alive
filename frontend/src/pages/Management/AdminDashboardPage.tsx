import { BookOpenCheck, CircleDollarSign, ShieldCheck, UsersRound } from "lucide-react";
import { useEffect, useState } from "react";
import Container from "@/components/common/Container";
import {
  CourseForm,
  InstructorAssignmentForm,
  PasswordForm,
  PaymentSettingsForm,
  ProgramForm,
} from "@/components/management/ManagementForms";
import { Button } from "@/components/ui/button";
import CourseRoster from "@/components/management/CourseRoster";
import { fetchAdminCourses, fetchCourseCategories, fetchUsers, publishCourse, type CourseCategory, type ManagementCourse } from "@/services/managementApi";
import { getRequestErrorMessage } from "@/services/requestError";

export default function AdminDashboardPage() {
  const [categories, setCategories] = useState<CourseCategory[]>([]);
  const [courses, setCourses] = useState<ManagementCourse[]>([]);
  const [userCount, setUserCount] = useState(0);
  const [feedback, setFeedback] = useState<string | null>(null);

  useEffect(() => {
    let active = true;
    Promise.allSettled([fetchCourseCategories(), fetchAdminCourses(), fetchUsers()]).then(([categoryResult, courseResult, usersResult]) => {
      if (!active) return;
      if (categoryResult.status === "fulfilled") setCategories(categoryResult.value);
      if (courseResult.status === "fulfilled") setCourses(courseResult.value);
      if (usersResult.status === "fulfilled") setUserCount(usersResult.value.length);
      const failed = [categoryResult, courseResult, usersResult].find((result) => result.status === "rejected");
      if (failed?.status === "rejected") setFeedback(getRequestErrorMessage(failed.reason, "Some management data could not be loaded."));
    });
    return () => { active = false; };
  }, []);

  async function togglePublish(course: ManagementCourse) {
    try {
      const publish = course.status !== "PUBLISHED";
      await publishCourse(course.id, publish);
      setCourses((items) => items.map((item) => item.id === course.id ? { ...item, status: publish ? "PUBLISHED" : "DRAFT" } : item));
    } catch (error) { setFeedback(getRequestErrorMessage(error, "Course status could not be updated.")); }
  }

  return <div className="min-h-screen bg-slate-50">
    <section className="bg-slate-950 py-14 text-white"><Container>
      <div className="flex flex-col gap-8 lg:flex-row lg:items-end lg:justify-between"><div><p className="text-sm font-bold uppercase tracking-[0.2em] text-blue-300">Administrator workspace</p><h1 className="mt-4 text-4xl font-bold tracking-tight sm:text-5xl">Operate HSSA from one clear workspace.</h1><p className="mt-4 max-w-3xl text-lg leading-8 text-slate-300">Manage people, learning content, live programs, publications, and course payment settings.</p></div><span className="inline-flex w-fit items-center gap-2 rounded-full border border-emerald-300/20 bg-emerald-400/10 px-4 py-2 text-sm font-semibold text-emerald-200"><ShieldCheck className="size-4" />Administrator access</span></div>
    </Container></section>
    <Container className="py-10">
      {feedback ? <p className="mb-6 rounded-xl bg-red-50 p-4 text-sm font-semibold text-red-700">{feedback}</p> : null}
      <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">{[
        [BookOpenCheck, "Courses", courses.length, "All catalogue records"],
        [UsersRound, "Registered users", userCount, "Role-managed accounts"],
        [ShieldCheck, "Published", courses.filter((course) => course.status === "PUBLISHED").length, "Visible to learners"],
        [CircleDollarSign, "Paid courses", courses.filter((course) => !course.isFree).length, "Admin payment policy"],
      ].map(([Icon, label, value, note]) => { const StatIcon = Icon as typeof BookOpenCheck; return <article key={String(label)} className="rounded-2xl border border-slate-200 bg-white p-5 card-lift"><StatIcon className="size-5 text-blue-600" /><p className="mt-5 text-3xl font-bold text-slate-950">{String(value)}</p><p className="mt-1 font-semibold text-slate-800">{String(label)}</p><p className="mt-1 text-xs text-slate-500">{String(note)}</p></article>; })}</div>

      <section className="mt-10"><div className="mb-5"><p className="text-sm font-bold uppercase tracking-[0.16em] text-blue-700">Catalogue control</p><h2 className="mt-2 text-2xl font-bold text-slate-950">Review and publish courses</h2></div><div className="overflow-hidden rounded-2xl border border-slate-200 bg-white card-lift"><div className="overflow-x-auto"><table className="w-full text-left text-sm"><thead className="bg-slate-100 text-xs uppercase tracking-wider text-slate-600"><tr><th className="px-5 py-4">Course</th><th className="px-5 py-4">Level</th><th className="px-5 py-4">Price</th><th className="px-5 py-4">Status</th><th className="px-5 py-4 text-right">Action</th></tr></thead><tbody className="divide-y divide-slate-100">{courses.map((course) => <tr key={course.id}><td className="px-5 py-4"><p className="font-semibold text-slate-950">{course.title}</p><p className="text-xs text-slate-500">{course.categoryName ?? "Uncategorised"}</p></td><td className="px-5 py-4 text-slate-600">{course.level.replaceAll("_", " ")}</td><td className="px-5 py-4 text-slate-600">{course.isFree ? "Free" : `${course.currency ?? "LKR"} ${course.price?.toLocaleString() ?? "—"}`}</td><td className="px-5 py-4"><span className={`rounded-full px-3 py-1 text-xs font-bold ${course.status === "PUBLISHED" ? "bg-emerald-50 text-emerald-700" : "bg-amber-50 text-amber-700"}`}>{course.status}</span></td><td className="px-5 py-4 text-right"><Button type="button" size="sm" variant="outline" onClick={() => togglePublish(course)}>{course.status === "PUBLISHED" ? "Unpublish" : "Publish"}</Button></td></tr>)}</tbody></table>{courses.length === 0 ? <p className="p-6 text-sm text-slate-500">No courses are available yet. Apply the competition seed or create one below.</p> : null}</div></div></section>

      <div className="mt-10"><CourseRoster courses={courses} /></div>

      <div className="mt-10 grid items-start gap-6 xl:grid-cols-2"><CourseForm categories={categories} onCreated={(course) => setCourses((items) => [course, ...items])} /><ProgramForm /><InstructorAssignmentForm courses={courses} /><PaymentSettingsForm courses={courses} /></div>
      <div className="mt-6"><PasswordForm /></div>
    </Container>
  </div>;
}
