import { Search, SlidersHorizontal } from "lucide-react";
import { useState } from "react";
import CourseCard from "@/components/courses/CourseCard";
import EmptyState from "@/components/common/EmptyState";
import Container from "@/components/common/Container";
import PageBanner from "@/components/common/PageBanner";
import Pagination from "@/components/common/Pagination";
import Reveal from "@/components/common/Reveal";
import SectionHeader from "@/components/common/SectionHeader";
import { courses } from "@/services/contentService";
import { uniqueValues } from "@/utils/format";

const ITEMS_PER_PAGE = 6;
const categories = ["All", ...uniqueValues(courses.map((course) => course.category))];

export default function CoursesPage() {
  const [query, setQuery] = useState("");
  const [category, setCategory] = useState("All");
  const [page, setPage] = useState(1);

  const normalizedQuery = query.trim().toLowerCase();

  const filteredCourses = courses.filter((course) => {
    const matchesCategory = category === "All" || course.category === category;
    const matchesQuery = [course.title, course.category, course.level, course.summary]
      .join(" ")
      .toLowerCase()
      .includes(normalizedQuery);

    return matchesCategory && matchesQuery;
  });

  const pageCount = Math.max(1, Math.ceil(filteredCourses.length / ITEMS_PER_PAGE));
  const activePage = Math.min(page, pageCount);
  const visibleCourses = filteredCourses.slice(
    (activePage - 1) * ITEMS_PER_PAGE,
    activePage * ITEMS_PER_PAGE,
  );

  return (
    <>
      <PageBanner
        eyebrow="Courses"
        title="Course pathways for humanities, social sciences, language, and professional growth."
        description="Search the mock catalogue by topic, level, and learning area. Every course record is stored in JSON for future backend integration."
      />

      <section className="py-16 sm:py-20">
        <Container>
          <SectionHeader
            eyebrow="Catalogue"
            title="Find your next learning path."
            description="Use the search and category filter to explore language learning, literature, research, school subjects, and professional skills."
          />

          <div className="mb-8 grid gap-4 rounded-lg border border-slate-200 bg-white p-4 shadow-sm lg:grid-cols-[1fr_260px]">
            <label className="relative block">
              <span className="sr-only">Search courses</span>
              <Search className="pointer-events-none absolute left-4 top-1/2 size-5 -translate-y-1/2 text-slate-400" />
              <input
                value={query}
                onChange={(event) => {
                  setQuery(event.target.value);
                  setPage(1);
                }}
                placeholder="Search courses, levels, or topics"
                className="h-12 w-full rounded-md border border-slate-200 bg-white pl-12 pr-4 text-sm outline-none transition placeholder:text-slate-400 focus:border-blue-500 focus:ring-3 focus:ring-blue-100"
              />
            </label>
            <label className="relative block">
              <span className="sr-only">Filter by category</span>
              <SlidersHorizontal className="pointer-events-none absolute left-4 top-1/2 size-5 -translate-y-1/2 text-slate-400" />
              <select
                value={category}
                onChange={(event) => {
                  setCategory(event.target.value);
                  setPage(1);
                }}
                className="h-12 w-full appearance-none rounded-md border border-slate-200 bg-white pl-12 pr-4 text-sm font-semibold text-slate-700 outline-none transition focus:border-blue-500 focus:ring-3 focus:ring-blue-100"
              >
                {categories.map((item) => (
                  <option key={item} value={item}>
                    {item}
                  </option>
                ))}
              </select>
            </label>
          </div>

          {visibleCourses.length > 0 ? (
            <div className="grid gap-5 md:grid-cols-2 xl:grid-cols-3">
              {visibleCourses.map((course, index) => (
                <Reveal key={course.id} delay={index * 0.04}>
                  <CourseCard course={course} />
                </Reveal>
              ))}
            </div>
          ) : (
            <EmptyState
              title="No courses found"
              description="Try a different keyword or category. The catalogue is mock data and can be expanded later from the backend."
            />
          )}

          <Pagination currentPage={activePage} pageCount={pageCount} onPageChange={setPage} />
        </Container>
      </section>
    </>
  );
}
