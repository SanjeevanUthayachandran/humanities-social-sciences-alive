import { ArrowRight, Newspaper } from "lucide-react";
import Badge from "@/components/common/Badge";
import { Button } from "@/components/ui/button";
import type { NewsItem } from "@/types/content";
import { formatDate } from "@/utils/format";

interface NewsCardProps {
  item: NewsItem;
}

export default function NewsCard({ item }: NewsCardProps) {
  return (
    <article className="flex h-full flex-col rounded-lg border border-slate-200 bg-white p-5 shadow-sm transition duration-300 hover:-translate-y-1 hover:border-blue-200 hover:shadow-xl">
      <div className="flex items-center justify-between gap-3">
        <Badge>{item.category}</Badge>
        <span className="text-xs font-semibold text-slate-500">{formatDate(item.date)}</span>
      </div>
      <div className="mt-5 flex items-start gap-3">
        <div className="grid size-10 shrink-0 place-items-center rounded-lg bg-blue-50 text-blue-700">
          <Newspaper className="size-5" />
        </div>
        <h3 className="text-xl font-bold leading-7 text-slate-950">{item.title}</h3>
      </div>
      <p className="mt-4 flex-1 text-sm leading-6 text-slate-600">{item.summary}</p>
      <div className="mt-6 flex items-center justify-between gap-4 border-t border-slate-100 pt-5">
        <span className="text-sm font-medium text-slate-500">{item.readTime}</span>
        <Button type="button" variant="outline" size="sm">
          Read
          <ArrowRight />
        </Button>
      </div>
    </article>
  );
}
