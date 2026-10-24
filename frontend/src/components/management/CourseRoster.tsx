import { GraduationCap, UsersRound } from "lucide-react";
import { useEffect, useState } from "react";
import { fetchCourseStudents, type CourseStudent, type ManagementCourse } from "@/services/managementApi";
import { getRequestErrorMessage } from "@/services/requestError";

export default function CourseRoster({ courses }: { courses: ManagementCourse[] }) {
  const [courseId, setCourseId] = useState("");
  const [students, setStudents] = useState<CourseStudent[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!courseId && courses.length) setCourseId(courses[0].id);
  }, [courseId, courses]);

  useEffect(() => {
    if (!courseId) { setStudents([]); return; }
    let active = true;
    setLoading(true);
    setError(null);
    fetchCourseStudents(courseId)
      .then((items) => { if (active) setStudents(items); })
      .catch((requestError) => { if (active) setError(getRequestErrorMessage(requestError, "Students could not be loaded.")); })
      .finally(() => { if (active) setLoading(false); });
    return () => { active = false; };
  }, [courseId]);

  return <section className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
    <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
      <div className="flex items-center gap-3"><span className="rounded-xl bg-cyan-50 p-2.5 text-cyan-700"><UsersRound className="size-5" /></span><div><h2 className="font-bold text-slate-950">Course registrations</h2><p className="text-sm text-slate-500">View every learner registered for a course.</p></div></div>
      <select value={courseId} onChange={(event) => setCourseId(event.target.value)} className="h-11 min-w-64 rounded-lg border border-slate-200 bg-white px-3 text-sm font-semibold text-slate-800 outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100">
        <option value="">{courses.length ? "Select course" : "No courses available"}</option>
        {courses.map((course) => <option key={course.id} value={course.id}>{course.title}</option>)}
      </select>
    </div>
    {error ? <p className="mt-5 rounded-lg bg-red-50 px-4 py-3 text-sm font-semibold text-red-700">{error}</p> : null}
    <div className="mt-6 overflow-x-auto rounded-xl border border-slate-200">
      <table className="w-full text-left text-sm"><thead className="bg-slate-100 text-xs uppercase tracking-wider text-slate-600"><tr><th className="px-4 py-3">Learner</th><th className="px-4 py-3">Email</th><th className="px-4 py-3">Status</th><th className="px-4 py-3">Registered</th></tr></thead><tbody className="divide-y divide-slate-100">
        {students.map((student) => <tr key={student.enrollmentId}><td className="px-4 py-3 font-semibold text-slate-950">{student.studentName}</td><td className="px-4 py-3 text-slate-600">{student.studentEmail}</td><td className="px-4 py-3"><span className="rounded-full bg-blue-50 px-2.5 py-1 text-xs font-bold text-blue-700">{student.status.replaceAll("_", " ")}</span></td><td className="px-4 py-3 text-slate-600">{new Date(student.enrolledAt).toLocaleDateString()}</td></tr>)}
      </tbody></table>
      {!loading && courseId && students.length === 0 ? <div className="flex items-center gap-2 p-5 text-sm text-slate-500"><GraduationCap className="size-4" />No learners are registered for this course yet.</div> : null}
      {loading ? <p className="p-5 text-sm text-slate-500">Loading registered learners…</p> : null}
      {!courseId ? <p className="p-5 text-sm text-slate-500">Choose a course to view its learners.</p> : null}
    </div>
  </section>;
}
