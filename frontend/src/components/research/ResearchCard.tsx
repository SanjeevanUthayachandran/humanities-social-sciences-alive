import { ArrowRight, Download, FileText } from "lucide-react";
import { Link } from "react-router-dom";
import Badge from "@/components/common/Badge";
import { buttonVariants } from "@/components/ui/buttonVariants";
import type { ResearchItem } from "@/types/content";
import { formatDate } from "@/utils/format";

interface ResearchCardProps {
  item: ResearchItem;
}

export default function ResearchCard({ item }: ResearchCardProps) {
  return (
    <article className="group flex h-full flex-col rounded-lg border border-slate-200 bg-white p-5 shadow-sm transition duration-300 hover:-translate-y-1 hover:border-blue-200 hover:shadow-xl">
      <div className="flex items-center justify-between gap-3">
        <Badge className="bg-emerald-50 text-emerald-700">{item.category}</Badge>
        <span className="text-xs font-semibold text-slate-500">{formatDate(item.date)}</span>
      </div>
      <h3 className="mt-5 text-xl font-bold leading-7 text-slate-950">{item.title}</h3>
      <p className="mt-3 flex-1 text-sm leading-6 text-slate-600">{item.summary}</p>
      <div className="mt-5 flex flex-wrap gap-2">
        {item.tags.slice(0, 3).map((tag) => (
          <span key={tag} className="rounded-md bg-slate-100 px-2 py-1 text-xs font-medium text-slate-600">
            {tag}
          </span>
        ))}
      </div>
      <div className="mt-6 flex items-center justify-between border-t border-slate-100 pt-5">
        <div className="flex items-center gap-2 text-sm font-medium text-slate-600">
          <Download className="size-4 text-blue-600" />
          {item.downloads.length} UI downloads
        </div>
        <Link
          to={`/research/${item.slug}`}
          className={buttonVariants({ variant: "outline", size: "sm" })}
        >
          Read
          <ArrowRight />
        </Link>
      </div>
      <div className="mt-4 flex items-center gap-2 text-xs font-semibold uppercase tracking-[0.14em] text-slate-400">
        <FileText className="size-3.5" />
        {item.author}
      </div>
    </article>
  );
}
