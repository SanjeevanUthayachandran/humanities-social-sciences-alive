import { Download, Search } from "lucide-react";
import { useState } from "react";
import EmptyState from "@/components/common/EmptyState";
import Container from "@/components/common/Container";
import PageBanner from "@/components/common/PageBanner";
import Reveal from "@/components/common/Reveal";
import SectionHeader from "@/components/common/SectionHeader";
import ResearchCard from "@/components/research/ResearchCard";
import { researchItems } from "@/services/contentService";
import { uniqueValues } from "@/utils/format";

export default function ResearchPage() {
  const [query, setQuery] = useState("");
  const [category, setCategory] = useState("All");

  const categories = ["All", ...uniqueValues(researchItems.map((item) => item.category))];
  const featuredResearch = researchItems.filter((item) => item.featured);
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
        title="Research, publications, and downloadable academic resources."
        description="A public research hub prepared for future editorial workflows, publication repositories, citations, and file downloads."
      />

      <section className="py-16 sm:py-20">
        <Container>
          <SectionHeader
            eyebrow="Featured research"
            title="Current research directions."
            description="These featured briefs show how the platform can support collaborative research and public academic resources."
          />
          <div className="grid gap-5 lg:grid-cols-2">
            {featuredResearch.map((item, index) => (
              <Reveal key={item.id} delay={index * 0.06}>
                <ResearchCard item={item} />
              </Reveal>
            ))}
          </div>
        </Container>
      </section>

      <section className="bg-slate-50 py-16 sm:py-20">
        <Container>
          <SectionHeader
            eyebrow="Browse research"
            title="Search by theme or category."
            description="The mock data is structured to become a backend-backed research repository with downloads, authors, and editorial controls."
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
              title="No research found"
              description="Try another search term or category. New research records can be added through JSON or future APIs."
            />
          )}
        </Container>
      </section>

      <section className="py-16 sm:py-20">
        <Container>
          <SectionHeader
            eyebrow="Downloads"
            title="Download centre preview."
            description="Download buttons are UI-only in this frontend release. They are ready for secure files, publication PDFs, and citation exports later."
          />
          <div className="grid gap-4 md:grid-cols-3">
            {["Research proposal template", "Citation guide", "Reading list format"].map((download) => (
              <article key={download} className="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
                <Download className="size-6 text-blue-600" />
                <h3 className="mt-4 font-bold text-slate-950">{download}</h3>
                <button
                  type="button"
                  className="mt-5 h-10 rounded-md border border-slate-200 px-4 text-sm font-semibold text-slate-700 transition hover:border-blue-200 hover:bg-blue-50 hover:text-blue-700"
                >
                  Download UI
                </button>
              </article>
            ))}
          </div>
        </Container>
      </section>
    </>
  );
}
