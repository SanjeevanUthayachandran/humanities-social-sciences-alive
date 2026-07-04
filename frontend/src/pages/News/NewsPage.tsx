import { Megaphone, Search } from "lucide-react";
import { useState } from "react";
import EmptyState from "@/components/common/EmptyState";
import Container from "@/components/common/Container";
import PageBanner from "@/components/common/PageBanner";
import Reveal from "@/components/common/Reveal";
import SectionHeader from "@/components/common/SectionHeader";
import NewsCard from "@/components/news/NewsCard";
import { newsItems } from "@/services/contentService";
import { uniqueValues } from "@/utils/format";

export default function NewsPage() {
  const [category, setCategory] = useState("All");
  const [query, setQuery] = useState("");
  const categories = ["All", ...uniqueValues(newsItems.map((item) => item.category))];
  const normalizedQuery = query.trim().toLowerCase();

  const filteredNews = newsItems.filter((item) => {
    const matchesCategory = category === "All" || item.category === category;
    const matchesQuery = [item.title, item.category, item.summary, item.body]
      .join(" ")
      .toLowerCase()
      .includes(normalizedQuery);

    return matchesCategory && matchesQuery;
  });

  const announcements = newsItems.filter((item) => item.featured);

  return (
    <>
      <PageBanner
        eyebrow="News"
        title="Latest announcements and platform updates."
        description="A newsroom page for course launches, platform progress, research desk updates, and workshop announcements."
      />

      <section className="py-16 sm:py-20">
        <Container>
          <SectionHeader
            eyebrow="Announcements"
            title="Important updates."
            description="Featured announcements help the public see what is changing across courses, research, and the platform roadmap."
          />
          <div className="grid gap-5 md:grid-cols-2">
            {announcements.map((item, index) => (
              <Reveal key={item.id} delay={index * 0.05}>
                <article className="rounded-lg border border-blue-100 bg-blue-50 p-6 shadow-sm">
                  <Megaphone className="size-7 text-blue-700" />
                  <h2 className="mt-4 text-xl font-bold text-slate-950">{item.title}</h2>
                  <p className="mt-3 text-sm leading-6 text-slate-700">{item.summary}</p>
                </article>
              </Reveal>
            ))}
          </div>
        </Container>
      </section>

      <section className="bg-slate-50 py-16 sm:py-20">
        <Container>
          <SectionHeader
            eyebrow="Newsroom"
            title="Browse all news."
            description="Filter by category or search by keyword. The newsroom is powered by JSON mock records."
          />
          <div className="mb-8 grid gap-4 rounded-lg border border-slate-200 bg-white p-4 shadow-sm lg:grid-cols-[1fr_240px]">
            <label className="relative block">
              <span className="sr-only">Search news</span>
              <Search className="pointer-events-none absolute left-4 top-1/2 size-5 -translate-y-1/2 text-slate-400" />
              <input
                value={query}
                onChange={(event) => setQuery(event.target.value)}
                placeholder="Search announcements"
                className="h-12 w-full rounded-md border border-slate-200 bg-white pl-12 pr-4 text-sm outline-none transition placeholder:text-slate-400 focus:border-blue-500 focus:ring-3 focus:ring-blue-100"
              />
            </label>
            <select
              value={category}
              onChange={(event) => setCategory(event.target.value)}
              className="h-12 rounded-md border border-slate-200 bg-white px-4 text-sm font-semibold text-slate-700 outline-none transition focus:border-blue-500 focus:ring-3 focus:ring-blue-100"
              aria-label="Filter news by category"
            >
              {categories.map((item) => (
                <option key={item} value={item}>
                  {item}
                </option>
              ))}
            </select>
          </div>

          {filteredNews.length > 0 ? (
            <div className="grid gap-5 md:grid-cols-2 xl:grid-cols-3">
              {filteredNews.map((item, index) => (
                <Reveal key={item.id} delay={index * 0.04}>
                  <NewsCard item={item} />
                </Reveal>
              ))}
            </div>
          ) : (
            <EmptyState
              title="No news found"
              description="Try another keyword or category. More announcements can be added to the mock JSON data."
            />
          )}
        </Container>
      </section>
    </>
  );
}
