import { Search } from "lucide-react";
import { useState } from "react";
import EmptyState from "@/components/common/EmptyState";
import Container from "@/components/common/Container";
import PageBanner from "@/components/common/PageBanner";
import Reveal from "@/components/common/Reveal";
import SectionHeader from "@/components/common/SectionHeader";
import ResearchCard from "@/components/research/ResearchCard";
import { useContent } from "@/hooks/useContent";
import { uniqueValues } from "@/utils/format";

export default function ResearchPage() {
  const { researchItems, isLoading, failedResources } = useContent();
  const [query, setQuery] = useState("");
  const [category, setCategory] = useState("All");

  const categories = ["All", ...uniqueValues(researchItems.map((item) => item.category))];
  const normalizedQuery = query.trim().toLowerCase();

  const filteredResearch = researchItems.filter((item) => {
    const matchesCategory = category === "All" || item.category === category;
    const matchesQuery = [item.title, item.category, item.summary, item.tags.join(" ")]
      .join(" ")
      .toLowerCase()
      .includes(normalizedQuery);

    return matchesCategory && matchesQuery;
  });

  return (
    <>
      <PageBanner
        eyebrow="Research"
        title="Research that begins with real questions."
        description="Explore HSSA research projects, from early inquiry to public findings."
      />

      <section className="py-16 sm:py-20">
        <Container>
          <SectionHeader
            eyebrow="Browse research"
            title="Search by theme or category."
            description="Find research by title, category, or theme."
          />

          <div className="mb-8 grid gap-4 rounded-lg border border-slate-200 bg-white p-4 shadow-sm lg:grid-cols-[1fr_260px]">
            <label className="relative block">
              <span className="sr-only">Search research</span>
              <Search className="pointer-events-none absolute left-4 top-1/2 size-5 -translate-y-1/2 text-slate-400" />
              <input
                value={query}
                onChange={(event) => setQuery(event.target.value)}
                placeholder="Search research topics"
                className="h-12 w-full rounded-md border border-slate-200 bg-white pl-12 pr-4 text-sm outline-none transition placeholder:text-slate-400 focus:border-blue-500 focus:ring-3 focus:ring-blue-100"
              />
            </label>
            <select
              value={category}
              onChange={(event) => setCategory(event.target.value)}
              className="h-12 rounded-md border border-slate-200 bg-white px-4 text-sm font-semibold text-slate-700 outline-none transition focus:border-blue-500 focus:ring-3 focus:ring-blue-100"
              aria-label="Filter research by category"
            >
              {categories.map((item) => (
                <option key={item} value={item}>
                  {item}
                </option>
              ))}
            </select>
          </div>

          {filteredResearch.length > 0 ? (
            <div className="grid gap-5 md:grid-cols-2">
              {filteredResearch.map((item, index) => (
                <Reveal key={item.id} delay={index * 0.04}>
                  <ResearchCard item={item} />
                </Reveal>
              ))}
            </div>
          ) : (
            <EmptyState
              title={isLoading ? "Loading research" : "No research found"}
              description={failedResources.includes("research") ? "Research could not be loaded. Please try again in a moment." : "Try another search term or category."}
            />
          )}
        </Container>
      </section>

    </>
  );
}
