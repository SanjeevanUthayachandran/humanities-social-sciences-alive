import type { TimelineItem } from "@/types/content";

interface TimelineProps {
  items: TimelineItem[];
}

export default function Timeline({ items }: TimelineProps) {
  return (
    <div className="grid gap-4 md:grid-cols-2">
      {items.map((item) => (
        <article key={`${item.year}-${item.title}`} className="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
          <span className="text-sm font-bold text-blue-700">{item.year}</span>
          <h3 className="mt-2 text-lg font-bold text-slate-950">{item.title}</h3>
          <p className="mt-2 text-sm leading-6 text-slate-600">{item.description}</p>
        </article>
      ))}
    </div>
  );
}
