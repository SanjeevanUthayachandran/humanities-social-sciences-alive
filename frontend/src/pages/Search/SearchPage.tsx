import { Search } from "lucide-react";
import { Link } from "react-router-dom";
import EmptyState from "@/components/common/EmptyState";
import Container from "@/components/common/Container";
import PageBanner from "@/components/common/PageBanner";
import SectionHeader from "@/components/common/SectionHeader";
import { buildSearchIndex, filterSearchResults } from "@/utils/search";
import { useMemo, useState } from "react";
import { useContent } from "@/hooks/useContent";

export default function SearchPage() {
  const { courses, researchItems, workshops, events, publications, books, isLoading } = useContent();
  const [query, setQuery] = useState("");
  const searchIndex = useMemo(
    () => buildSearchIndex({ courses, researchItems, workshops, events, publications, books }),
    [courses, researchItems, workshops, events, publications, books],
  );
  const results = filterSearchResults(searchIndex, query);

  return (
    <>
      <PageBanner
        eyebrow="Search"
        title="Search the whole HSSA learning ecosystem."
        description="Find live courses, research, workshops, events, publications, and reading-club books in one place."
      />

      <section className="py-16 sm:py-20">
        <Container>
          <label className="relative block">
            <span className="sr-only">Search the website</span>
            <Search className="pointer-events-none absolute left-5 top-1/2 size-6 -translate-y-1/2 text-slate-400" />
            <input
              value={query}
              onChange={(event) => setQuery(event.target.value)}
              placeholder="Search for courses, research, workshops, or events"
              className="h-14 w-full rounded-lg border border-slate-200 bg-white pl-14 pr-4 text-base outline-none transition placeholder:text-slate-400 focus:border-blue-500 focus:ring-3 focus:ring-blue-100"
            />
          </label>

          <div className="mt-10">
              <SectionHeader
                eyebrow="Results"
                title={query ? `Results for "${query}"` : "Browse everything"}
                description={`${results.length} matching items from the site index.`}
                className="mb-6"
              />
              {results.length > 0 ? (
                <div className="grid gap-4">
                  {results.map((result) => (
                    <Link
                      key={`${result.type}-${result.id}`}
                      to={result.path}
                      className="rounded-2xl border border-slate-200 bg-white p-6 card-lift"
                    >
                      <span className="text-xs font-bold uppercase tracking-[0.14em] text-blue-700">
                        {result.type}
                      </span>
                      <h2 className="mt-2 text-xl font-bold text-slate-950">{result.title}</h2>
                      <p className="mt-2 text-sm leading-6 text-slate-600">{result.summary}</p>
                    </Link>
                  ))}
                </div>
              ) : (
                <EmptyState
                  title="No results found"
                  description={isLoading ? "The catalogue is still loading." : "Try another keyword or browse a section from the main navigation."}
                />
              )}
          </div>
        </Container>
      </section>
    </>
  );
}
