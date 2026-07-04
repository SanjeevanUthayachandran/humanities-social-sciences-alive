import { ArrowLeft, Download, FileText, Tag } from "lucide-react";
import { Link, useParams } from "react-router-dom";
import Container from "@/components/common/Container";
import PageBanner from "@/components/common/PageBanner";
import SectionHeader from "@/components/common/SectionHeader";
import { buttonVariants } from "@/components/ui/buttonVariants";
import { getResearchBySlug } from "@/services/contentService";
import { formatDate } from "@/utils/format";

export default function ResearchDetailsPage() {
  const { slug } = useParams();
  const item = getResearchBySlug(slug);

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
            <p className="mt-3 text-slate-600">The selected research item is not available in the mock data.</p>
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
                  description="Tags and categories are ready for future filtering and recommendation APIs."
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
                <h2 className="text-xl font-bold text-slate-950">Downloads</h2>
              </div>
              <p className="mt-3 text-sm leading-6 text-slate-600">
                These controls are UI-only placeholders for protected documents and citation files.
              </p>
              <div className="mt-6 space-y-3">
                {item.downloads.map((download) => (
                  <button
                    key={download}
                    type="button"
                    className="flex h-11 w-full items-center justify-between rounded-md border border-slate-200 px-4 text-sm font-semibold text-slate-700 transition hover:border-blue-200 hover:bg-blue-50 hover:text-blue-700"
                  >
                    {download}
                    <Download className="size-4" />
                  </button>
                ))}
              </div>
            </aside>
          </div>
        </Container>
      </section>
    </>
  );
}
