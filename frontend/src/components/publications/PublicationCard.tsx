import { ArrowUpRight, BookMarked, CalendarDays } from "lucide-react";
import { Link } from "react-router-dom";
import Badge from "@/components/common/Badge";
import { buttonVariants } from "@/components/ui/buttonVariants";
import type { PublicationItem } from "@/types/content";
import { formatDate } from "@/utils/format";

export default function PublicationCard({ publication }: { publication: PublicationItem }) {
  return (
    <article className="group flex h-full flex-col rounded-2xl border border-slate-200 bg-white p-6 card-lift">
      <div className="flex items-start justify-between gap-4">
        <Badge className="bg-violet-50 text-violet-700">{publication.type}</Badge>
        <BookMarked className="size-5 text-slate-400 transition group-hover:text-blue-600" />
      </div>
      <p className="mt-5 text-xs font-bold uppercase tracking-[0.16em] text-slate-500">{publication.category}</p>
      <h3 className="mt-2 text-xl font-bold leading-7 text-slate-950">{publication.title}</h3>
      <p className="mt-3 line-clamp-3 flex-1 text-sm leading-6 text-slate-600">{publication.summary}</p>
      <div className="mt-6 flex items-center justify-between gap-4 border-t border-slate-100 pt-5">
        <span className="inline-flex items-center gap-2 text-sm text-slate-500">
          <CalendarDays className="size-4 text-blue-600" />
          {formatDate(publication.date)}
        </span>
        <Link to={`/publications/${publication.id}`} className={buttonVariants({ variant: "outline", size: "sm" })}>
          Read
          <ArrowUpRight />
        </Link>
      </div>
    </article>
  );
}
