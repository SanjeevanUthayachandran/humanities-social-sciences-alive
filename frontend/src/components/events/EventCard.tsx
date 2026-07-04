import { CalendarDays, MapPin } from "lucide-react";
import Badge from "@/components/common/Badge";
import type { EventItem } from "@/types/content";
import { formatDate } from "@/utils/format";

interface EventCardProps {
  event: EventItem;
}

export default function EventCard({ event }: EventCardProps) {
  return (
    <article className="rounded-lg border border-slate-200 bg-white p-5 shadow-sm transition duration-300 hover:-translate-y-1 hover:border-blue-200 hover:shadow-xl">
      <div className="flex items-center justify-between gap-3">
        <Badge className="bg-emerald-50 text-emerald-700">{event.type}</Badge>
        {event.featured ? <span className="text-xs font-semibold text-blue-700">Featured</span> : null}
      </div>
      <h3 className="mt-5 text-xl font-bold leading-7 text-slate-950">{event.title}</h3>
      <p className="mt-3 text-sm leading-6 text-slate-600">{event.summary}</p>
      <div className="mt-5 grid gap-3 text-sm text-slate-600 sm:grid-cols-2">
        <div className="flex items-center gap-2">
          <CalendarDays className="size-4 text-blue-600" />
          <span>{formatDate(event.date)} at {event.time}</span>
        </div>
        <div className="flex items-center gap-2">
          <MapPin className="size-4 text-emerald-600" />
          <span>{event.location}</span>
        </div>
      </div>
    </article>
  );
}
