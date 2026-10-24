import { ArrowLeft, BookOpen, CheckCircle2, Clock, MonitorPlay, Users } from "lucide-react";
import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import Container from "@/components/common/Container";
import PageBanner from "@/components/common/PageBanner";
import SectionHeader from "@/components/common/SectionHeader";
import { Button } from "@/components/ui/button";
import { buttonVariants } from "@/components/ui/buttonVariants";
import PageLoader from "@/components/common/PageLoader";
import { useContent } from "@/hooks/useContent";
import { enrollInCourse, fetchCourseBySlug } from "@/services/contentApi";
import { getRequestErrorMessage } from "@/services/requestError";
import type { Course } from "@/types/content";

export default function CourseDetailsPage() {
  const { slug } = useParams();
  const { courses } = useContent();
  const fallbackCourse = courses.find((item) => item.slug === slug);
  const [course, setCourse] = useState<Course | undefined>(fallbackCourse);
  const [isLoading, setIsLoading] = useState(!fallbackCourse);
  const [isEnrolling, setIsEnrolling] = useState(false);
  const [enrollmentFeedback, setEnrollmentFeedback] = useState<{ type: "success" | "error"; text: string } | null>(null);
  const isAuthenticated = Boolean(localStorage.getItem("hssa_access_token") ?? sessionStorage.getItem("hssa_access_token"));

  async function handleEnrollment() {
    if (!course) return;
    setIsEnrolling(true);
    setEnrollmentFeedback(null);
    try {
      await enrollInCourse(course.id);
      setEnrollmentFeedback({ type: "success", text: "You are enrolled in this course." });
    } catch (error) {
      setEnrollmentFeedback({ type: "error", text: getRequestErrorMessage(error, "Enrollment could not be completed.") });
    } finally {
      setIsEnrolling(false);
    }
  }

  useEffect(() => {
    if (!slug) return;
    let active = true;
    fetchCourseBySlug(slug)
      .then((result) => {
        if (active) setCourse(result);
      })
      .catch(() => {
        if (active) setCourse(fallbackCourse);
      })
      .finally(() => {
        if (active) setIsLoading(false);
      });
    return () => {
      active = false;
    };
  }, [slug, fallbackCourse]);

  if (isLoading) return <PageLoader />;

  if (!course) {
    return (
      <section className="py-20">
        <Container>
          <Link to="/courses" className={buttonVariants({ variant: "outline" })}>
            <ArrowLeft />
            Back to Courses
          </Link>
          <div className="mt-8 rounded-lg border border-dashed border-slate-300 bg-white p-8">
            <h1 className="text-2xl font-bold text-slate-950">Course not found</h1>
            <p className="mt-3 text-slate-600">The selected course is not available.</p>
          </div>
        </Container>
      </section>
    );
  }

  return (
    <>
      <PageBanner eyebrow={course.category} title={course.title} description={course.summary}>
        <Link to="/courses" className={buttonVariants({ variant: "outline" })}>
          <ArrowLeft />
          Back to Courses
        </Link>
      </PageBanner>

      <section className="py-16 sm:py-20">
        <Container>
          <div className="grid gap-8 lg:grid-cols-[1fr_360px]">
            <div>
              <SectionHeader
                eyebrow="Course focus"
                title="What this course explores."
                description="Topics and modules are loaded from the published course record."
              />
              {course.outcomes.length > 0 ? <div className="grid gap-4 sm:grid-cols-2">
                {course.outcomes.map((outcome) => (
                  <article key={outcome} className="flex gap-3 rounded-2xl border border-slate-200 bg-white p-6 card-lift">
                    <CheckCircle2 className="mt-0.5 size-5 shrink-0 text-emerald-600" />
                    <p className="text-sm leading-6 text-slate-700">{outcome}</p>
                  </article>
                ))}
              </div> : <p className="text-slate-600">Learning outcomes will be published with the course syllabus.</p>}

              {course.modules.length > 0 ? <div className="mt-12">
                <SectionHeader
                  eyebrow="Modules"
                  title="Course structure."
                  description="Follow the published module sequence from foundation to application."
                />
                <div className="grid gap-4">
                  {course.modules.map((module, index) => (
                    <article key={module} className="rounded-2xl border border-slate-200 bg-white p-6 card-lift">
                      <span className="text-sm font-bold text-blue-700">Module {index + 1}</span>
                      <h3 className="mt-2 text-lg font-bold text-slate-950">{module}</h3>
                    </article>
                  ))}
                </div>
              </div> : null}
            </div>

            <aside className="h-fit rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
              <h2 className="text-xl font-bold text-slate-950">Course snapshot</h2>
              <dl className="mt-6 space-y-4 text-sm text-slate-600">
                <div className="flex items-center gap-3">
                  <Clock className="size-5 text-blue-600" />
                  <span>{course.duration}</span>
                </div>
                <div className="flex items-center gap-3">
                  <BookOpen className="size-5 text-emerald-600" />
                  <span>{course.level}</span>
                </div>
                <div className="flex items-center gap-3">
                  <MonitorPlay className="size-5 text-blue-600" />
                  <span>{course.mode}</span>
                </div>
                {course.learners > 0 ? <div className="flex items-center gap-3">
                  <Users className="size-5 text-slate-500" />
                  <span>{course.learners}+ interested learners</span>
                </div> : null}
              </dl>
              <div className="mt-6 rounded-lg bg-blue-50 p-4">
                <p className="text-sm font-semibold text-blue-800">{course.priceLabel}</p>
              </div>
              {isAuthenticated ? (
                <Button type="button" onClick={handleEnrollment} disabled={isEnrolling || enrollmentFeedback?.type === "success"} className="mt-5 h-11 w-full bg-blue-600 hover:bg-blue-700">
                  {isEnrolling ? "Enrolling..." : enrollmentFeedback?.type === "success" ? "Enrolled" : "Enrol Now"}
                </Button>
              ) : (
                <Link to="/login" className={buttonVariants({ className: "mt-5 h-11 w-full bg-blue-600 hover:bg-blue-700" })}>Login to Enrol</Link>
              )}
              {enrollmentFeedback ? <p role="status" className={`mt-3 text-sm font-semibold ${enrollmentFeedback.type === "success" ? "text-emerald-700" : "text-red-700"}`}>{enrollmentFeedback.text}</p> : null}
            </aside>
          </div>
        </Container>
      </section>
    </>
  );
}
