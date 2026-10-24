import { ArrowLeft, CalendarDays, FileText, Tag, UserRound } from "lucide-react";
import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import Container from "@/components/common/Container";
import PageBanner from "@/components/common/PageBanner";
import SectionHeader from "@/components/common/SectionHeader";
import { buttonVariants } from "@/components/ui/buttonVariants";
import PageLoader from "@/components/common/PageLoader";
import { useContent } from "@/hooks/useContent";
import { fetchResearchBySlug } from "@/services/contentApi";
import type { ResearchItem } from "@/types/content";
import { formatDate } from "@/utils/format";

export default function ResearchDetailsPage() {
  const { slug } = useParams();
  const { researchItems } = useContent();
  const fallbackItem = researchItems.find((research) => research.slug === slug);
  const [item, setItem] = useState<ResearchItem | undefined>(fallbackItem);
  const [isLoading, setIsLoading] = useState(!fallbackItem);

  useEffect(() => {
    if (!slug) return;
    let active = true;
    fetchResearchBySlug(slug)
      .then((result) => {
        if (active) setItem(result);
      })
      .catch(() => {
        if (active) setItem(fallbackItem);
      })
      .finally(() => {
        if (active) setIsLoading(false);
      });
    return () => {
      active = false;
    };
  }, [slug, fallbackItem]);

  if (isLoading) return <PageLoader />;

  if (!item) {
    return (
      <section className="py-20">
        <Container>
          <Link to="/research" className={buttonVariants({ variant: "outline" })}>
            <ArrowLeft />
            Back to Research
          </Link>
          <div className="mt-8 rounded-lg border border-dashed border-slate-300 bg-white p-8">
            <h1 className="text-2xl font-bold text-slate-950">Research item not found</h1>
            <p className="mt-3 text-slate-600">The selected research item is not available.</p>
          </div>
        </Container>
      </section>
    );
  }

  return (
    <>
      <PageBanner eyebrow={item.category} title={item.title} description={item.summary}>
        <Link to="/research" className={buttonVariants({ variant: "outline" })}>
          <ArrowLeft />
          Back to Research
        </Link>
      </PageBanner>

      <section className="py-16 sm:py-20">
        <Container>
          <div className="grid gap-8 lg:grid-cols-[1fr_340px]">
            <article className="rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
              <div className="flex flex-wrap gap-3 text-sm font-semibold text-slate-600">
                <span>{item.author}</span>
                <span>{formatDate(item.date)}</span>
              </div>
              <h2 className="mt-6 text-2xl font-bold text-slate-950">Abstract</h2>
              <p className="mt-4 leading-8 text-slate-600">{item.abstract}</p>
              <div className="mt-8">
                <SectionHeader
                  eyebrow="Tags"
                  title="Research themes."
                  description="Themes published with this research record."
                  className="mb-5"
                />
                <div className="flex flex-wrap gap-2">
                  {item.tags.map((tag) => (
                    <span key={tag} className="inline-flex items-center gap-2 rounded-md bg-slate-100 px-3 py-1.5 text-sm font-semibold text-slate-700">
                      <Tag className="size-3.5" />
                      {tag}
                    </span>
                  ))}
                </div>
              </div>
            </article>

            <aside className="h-fit rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
              <div className="flex items-center gap-3">
                <FileText className="size-6 text-blue-600" />
                <h2 className="text-xl font-bold text-slate-950">Project details</h2>
              </div>
              <dl className="mt-6 space-y-4 text-sm text-slate-600">
                <div className="flex gap-3"><UserRound className="mt-0.5 size-4 shrink-0 text-blue-600" /><div><dt className="font-semibold text-slate-950">Lead contributor</dt><dd className="mt-1">{item.author}</dd></div></div>
                <div className="flex gap-3"><CalendarDays className="mt-0.5 size-4 shrink-0 text-emerald-600" /><div><dt className="font-semibold text-slate-950">Published</dt><dd className="mt-1">{formatDate(item.date)}</dd></div></div>
                <div className="flex gap-3"><Tag className="mt-0.5 size-4 shrink-0 text-blue-600" /><div><dt className="font-semibold text-slate-950">Category</dt><dd className="mt-1">{item.category}</dd></div></div>
              </dl>
            </aside>
          </div>
        </Container>
      </section>
    </>
  );
}
