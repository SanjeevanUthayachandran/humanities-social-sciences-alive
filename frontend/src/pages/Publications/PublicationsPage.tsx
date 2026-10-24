import { Search } from "lucide-react";
import { useState } from "react";
import Container from "@/components/common/Container";
import EmptyState from "@/components/common/EmptyState";
import PageBanner from "@/components/common/PageBanner";
import Reveal from "@/components/common/Reveal";
import SectionHeader from "@/components/common/SectionHeader";
import PublicationCard from "@/components/publications/PublicationCard";
import { useContent } from "@/hooks/useContent";
import { uniqueValues } from "@/utils/format";

export default function PublicationsPage() {
  const { publications, isLoading, failedResources } = useContent();
  const [query, setQuery] = useState("");
  const [type, setType] = useState("All");
  const types = ["All", ...uniqueValues(publications.map((item) => item.type))];
  const normalizedQuery = query.trim().toLowerCase();
  const visiblePublications = publications.filter((item) =>
    (type === "All" || item.type === type)
    && [item.title, item.summary, item.category, item.type].join(" ").toLowerCase().includes(normalizedQuery),
  );

  return (
    <>
      <PageBanner
        eyebrow="Publications"
        title="Ideas shaped for public use."
        description="Explore articles, reports, books, and academic work published by HSSA."
      />
      <section className="py-16 sm:py-20">
        <Container>
          <SectionHeader
            eyebrow="Library"
            title="Browse published work."
            description="Articles, reports, and academic work from the HSSA community."
          />
          <div className="mb-8 grid gap-4 rounded-2xl border border-slate-200 bg-white p-4 shadow-sm lg:grid-cols-[1fr_240px]">
            <label className="relative block">
              <span className="sr-only">Search publications</span>
              <Search className="pointer-events-none absolute left-4 top-1/2 size-5 -translate-y-1/2 text-slate-400" />
              <input value={query} onChange={(event) => setQuery(event.target.value)} placeholder="Search publications" className="h-12 w-full rounded-xl border border-slate-200 pl-12 pr-4 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
            </label>
            <select value={type} onChange={(event) => setType(event.target.value)} className="h-12 rounded-xl border border-slate-200 bg-white px-4 text-sm font-semibold text-slate-700 outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" aria-label="Filter publications by type">
              {types.map((item) => <option key={item}>{item}</option>)}
            </select>
          </div>
          {visiblePublications.length ? (
            <div className="grid gap-5 md:grid-cols-2 xl:grid-cols-3">
              {visiblePublications.map((publication, index) => (
                <Reveal key={publication.id} delay={index * 0.04}><PublicationCard publication={publication} /></Reveal>
              ))}
            </div>
          ) : (
            <EmptyState
              title={isLoading ? "Loading publications" : "No publications found"}
              description={failedResources.includes("publications") ? "Publications could not be loaded. Please try again in a moment." : "Published work will appear here when it is available."}
            />
          )}
        </Container>
      </section>
    </>
  );
}
