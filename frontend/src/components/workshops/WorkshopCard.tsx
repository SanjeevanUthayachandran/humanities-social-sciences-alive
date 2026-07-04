import { CalendarDays, MonitorPlay, Users } from "lucide-react";
import Badge from "@/components/common/Badge";
import { Button } from "@/components/ui/button";
import type { Workshop } from "@/types/content";
import { formatDate } from "@/utils/format";

interface WorkshopCardProps {
  workshop: Workshop;
}

export default function WorkshopCard({ workshop }: WorkshopCardProps) {
  return (
    <article className="flex h-full flex-col rounded-lg border border-slate-200 bg-white p-5 shadow-sm transition duration-300 hover:-translate-y-1 hover:border-blue-200 hover:shadow-xl">
      <div className="flex items-center justify-between gap-3">
        <Badge className={workshop.status === "Upcoming" ? "bg-blue-50 text-blue-700" : "bg-slate-100 text-slate-600"}>
          {workshop.status}
        </Badge>
        <span className="text-xs font-semibold uppercase tracking-[0.14em] text-slate-500">
          {workshop.category}
        </span>
      </div>
      <h3 className="mt-5 text-xl font-bold leading-7 text-slate-950">{workshop.title}</h3>
      <p className="mt-3 flex-1 text-sm leading-6 text-slate-600">{workshop.summary}</p>
      <dl className="mt-5 space-y-3 text-sm text-slate-600">
        <div className="flex items-center gap-2">
          <CalendarDays className="size-4 text-blue-600" />
          <span>{formatDate(workshop.date)} at {workshop.time}</span>
        </div>
        <div className="flex items-center gap-2">
          <MonitorPlay className="size-4 text-emerald-600" />
          <span>{workshop.format}</span>
        </div>
        <div className="flex items-center gap-2">
          <Users className="size-4 text-slate-500" />
          <span>{workshop.seats} seats</span>
        </div>
      </dl>
      <div className="mt-6 flex items-center justify-between gap-4 border-t border-slate-100 pt-5">
        <span className="text-sm font-semibold text-slate-700">{workshop.priceLabel}</span>
        <Button type="button" size="sm" disabled={workshop.status === "Past"} className="bg-blue-600 hover:bg-blue-700">
          Register
        </Button>
      </div>
    </article>
  );
}
