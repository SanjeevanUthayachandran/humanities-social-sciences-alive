import { ArrowLeft, BookMarked, CalendarDays, ExternalLink, Tag } from "lucide-react";
import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import Container from "@/components/common/Container";
import PageBanner from "@/components/common/PageBanner";
import PageLoader from "@/components/common/PageLoader";
import { buttonVariants } from "@/components/ui/buttonVariants";
import { useContent } from "@/hooks/useContent";
import { fetchPublicationById } from "@/services/contentApi";
import type { PublicationItem } from "@/types/content";
import { formatDate } from "@/utils/format";

export default function PublicationDetailsPage() {
  const { id } = useParams();
  const { publications } = useContent();
  const summary = publications.find((item) => item.id === id);
  const [publication, setPublication] = useState<PublicationItem | undefined>(summary);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    if (!id) return;
    let active = true;
    fetchPublicationById(id)
      .then((result) => { if (active) setPublication(result); })
      .catch(() => { if (active) setPublication(summary); })
      .finally(() => { if (active) setIsLoading(false); });
    return () => { active = false; };
  }, [id, summary]);

  if (isLoading) return <PageLoader />;
  if (!publication) return <section className="py-20"><Container><p className="text-slate-600">This publication is not available.</p></Container></section>;

  return (
    <>
      <PageBanner eyebrow={publication.type} title={publication.title} description={publication.summary}>
        <Link to="/publications" className={buttonVariants({ variant: "outline" })}><ArrowLeft />Back to Publications</Link>
      </PageBanner>
      <section className="py-16 sm:py-20">
        <Container>
          <div className="grid gap-8 lg:grid-cols-[1fr_320px]">
            <article className="rounded-2xl border border-slate-200 bg-white p-7 shadow-sm">
              <div className="max-w-none whitespace-pre-line text-base leading-8 text-slate-700">
                {publication.content || publication.summary}
              </div>
              {publication.keywords.length ? <div className="mt-8 flex flex-wrap gap-2 border-t border-slate-100 pt-6">{publication.keywords.map((keyword) => <span key={keyword} className="inline-flex items-center gap-1.5 rounded-full bg-slate-100 px-3 py-1.5 text-sm font-semibold text-slate-700"><Tag className="size-3.5" />{keyword}</span>)}</div> : null}
            </article>
            <aside className="h-fit rounded-2xl bg-slate-950 p-6 text-white shadow-xl">
              <BookMarked className="size-7 text-emerald-300" />
              <h2 className="mt-4 text-xl font-bold">Publication details</h2>
              <dl className="mt-6 space-y-4 text-sm text-slate-300">
                <div><dt className="font-semibold text-white">Category</dt><dd className="mt-1">{publication.category}</dd></div>
                <div><dt className="font-semibold text-white">Published</dt><dd className="mt-1 inline-flex items-center gap-2"><CalendarDays className="size-4" />{formatDate(publication.date)}</dd></div>
                {publication.authors.length ? <div><dt className="font-semibold text-white">Authors</dt><dd className="mt-1">{publication.authors.join(", ")}</dd></div> : null}
              </dl>
              {publication.doi ? <a href={`https://doi.org/${publication.doi}`} target="_blank" rel="noreferrer" className="mt-6 inline-flex items-center gap-2 text-sm font-semibold text-emerald-300 hover:text-emerald-200">Open DOI<ExternalLink className="size-4" /></a> : null}
            </aside>
          </div>
        </Container>
      </section>
    </>
  );
}
