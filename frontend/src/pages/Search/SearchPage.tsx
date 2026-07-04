import { Clock3, Search } from "lucide-react";
import { Link } from "react-router-dom";
import EmptyState from "@/components/common/EmptyState";
import Container from "@/components/common/Container";
import PageBanner from "@/components/common/PageBanner";
import SectionHeader from "@/components/common/SectionHeader";
import { buttonVariants } from "@/components/ui/buttonVariants";
import { popularTopics, recentSearches } from "@/services/contentService";
import { buildSearchIndex, filterSearchResults } from "@/utils/search";
import { useState } from "react";

const searchIndex = buildSearchIndex();

export default function SearchPage() {
  const [query, setQuery] = useState("");
  const results = filterSearchResults(searchIndex, query);

  return (
    <>
      <PageBanner
        eyebrow="Search"
        title="Search courses, research, workshops, events, and news."
        description="A global search page using a local mock index today and ready for a backend search endpoint later."
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

          <div className="mt-10 grid gap-8 lg:grid-cols-[1fr_320px]">
            <div>
              <SectionHeader
                eyebrow="Results"
                title={query ? `Results for "${query}"` : "Popular results"}
                description={`${results.length} matching items from the mock search index.`}
                className="mb-6"
              />
              {results.length > 0 ? (
                <div className="grid gap-4">
                  {results.map((result) => (
                    <Link
                      key={`${result.type}-${result.id}`}
                      to={result.path}
                      className="rounded-lg border border-slate-200 bg-white p-5 shadow-sm transition hover:-translate-y-1 hover:border-blue-200 hover:shadow-xl"
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
                  description="Try another keyword. Future backend search can support ranking, filters, and saved searches."
                />
              )}
            </div>

            <aside className="space-y-5">
              <article className="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
                <div className="flex items-center gap-2">
                  <Clock3 className="size-5 text-blue-600" />
                  <h2 className="font-bold text-slate-950">Recent searches</h2>
                </div>
                <div className="mt-5 flex flex-wrap gap-2">
                  {recentSearches.map((topic) => (
                    <Link key={topic.label} to={topic.path} className={buttonVariants({ variant: "outline", size: "sm" })}>
                      {topic.label}
                    </Link>
                  ))}
                </div>
              </article>
              <article className="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
                <h2 className="font-bold text-slate-950">Popular topics</h2>
                <div className="mt-5 flex flex-wrap gap-2">
                  {popularTopics.map((topic) => (
                    <Link key={topic.label} to={topic.path} className={buttonVariants({ variant: "outline", size: "sm" })}>
                      {topic.label}
                    </Link>
                  ))}
                </div>
              </article>
            </aside>
          </div>
        </Container>
      </section>
    </>
  );
}
