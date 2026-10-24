import { BookOpenText, Library } from "lucide-react";
import Badge from "@/components/common/Badge";
import type { BookItem } from "@/types/content";

export default function BookCard({ book }: { book: BookItem }) {
  return (
    <article className="group flex h-full flex-col rounded-2xl border border-slate-200 bg-white p-6 card-lift">
      <div className="flex items-start justify-between gap-4">
        <div className="grid size-11 place-items-center rounded-xl bg-emerald-50 text-emerald-700">
          <BookOpenText className="size-5" />
        </div>
        {book.featured ? <Badge className="bg-amber-50 text-amber-700">Featured</Badge> : null}
      </div>
      <p className="mt-5 text-xs font-bold uppercase tracking-[0.16em] text-emerald-700">{book.category}</p>
      <h3 className="mt-2 text-xl font-bold leading-7 text-slate-950">{book.title}</h3>
      <p className="mt-3 flex-1 text-sm leading-6 text-slate-600">by {book.author}</p>
      <div className="mt-6 flex items-center gap-2 border-t border-slate-100 pt-5 text-sm text-slate-500">
        <Library className="size-4 text-blue-600" />
        {book.isbn ? `ISBN ${book.isbn}` : "Reading-club catalogue"}
      </div>
    </article>
  );
}
