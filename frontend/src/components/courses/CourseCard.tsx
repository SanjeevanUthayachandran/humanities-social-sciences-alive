import { ArrowRight, BookOpen, Clock, Star, Users } from "lucide-react";
import { Link } from "react-router-dom";
import Badge from "@/components/common/Badge";
import { buttonVariants } from "@/components/ui/buttonVariants";
import type { Course } from "@/types/content";

interface CourseCardProps {
  course: Course;
}

export default function CourseCard({ course }: CourseCardProps) {
  return (
    <article className="group flex h-full flex-col rounded-lg border border-slate-200 bg-white p-5 shadow-sm transition duration-300 hover:-translate-y-1 hover:border-blue-200 hover:shadow-xl">
      <div className="flex items-start justify-between gap-4">
        <Badge>{course.category}</Badge>
        {course.rating > 0 ? <div className="flex items-center gap-1 text-sm font-semibold text-amber-600">
          <Star className="size-4 fill-amber-400 text-amber-400" />
          {course.rating.toFixed(1)}
        </div> : null}
      </div>
      <h3 className="mt-5 text-xl font-bold leading-7 text-slate-950">{course.title}</h3>
      <p className="mt-3 flex-1 text-sm leading-6 text-slate-600">{course.summary}</p>
      <dl className="mt-5 grid grid-cols-2 gap-3 text-sm text-slate-600">
        <div className="flex items-center gap-2">
          <Clock className="size-4 text-blue-600" />
          <span>{course.duration}</span>
        </div>
        <div className="flex items-center gap-2">
          <BookOpen className="size-4 text-emerald-600" />
          <span>{course.level}</span>
        </div>
        {course.learners > 0 ? <div className="col-span-2 flex items-center gap-2">
          <Users className="size-4 text-slate-500" />
          <span>{course.learners}+ learners interested</span>
        </div> : null}
      </dl>
      <div className="mt-6 flex items-center justify-between gap-4 border-t border-slate-100 pt-5">
        <span className="text-sm font-semibold text-slate-700">{course.priceLabel}</span>
        <Link
          to={`/courses/${course.slug}`}
          className={buttonVariants({ variant: "outline", size: "sm" })}
        >
          Details
          <ArrowRight />
        </Link>
      </div>
    </article>
  );
}
