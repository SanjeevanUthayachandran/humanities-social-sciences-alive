import { HelpCircle } from "lucide-react";
import type { FaqItem } from "@/types/content";

interface FaqListProps {
  items: FaqItem[];
}

export default function FaqList({ items }: FaqListProps) {
  return (
    <div className="grid gap-4 md:grid-cols-2">
      {items.map((item) => (
        <article key={item.question} className="rounded-2xl border border-slate-200 bg-white p-6 card-lift">
          <div className="flex items-start gap-3">
            <HelpCircle className="mt-0.5 size-5 shrink-0 text-blue-600" />
            <div>
              <h3 className="font-bold text-slate-950">{item.question}</h3>
              <p className="mt-2 text-sm leading-6 text-slate-600">{item.answer}</p>
            </div>
          </div>
        </article>
      ))}
    </div>
  );
}
