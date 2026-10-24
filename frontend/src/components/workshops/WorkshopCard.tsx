import { CalendarDays, MonitorPlay, Users } from "lucide-react";
import { useState } from "react";
import { Link } from "react-router-dom";
import Badge from "@/components/common/Badge";
import { Button } from "@/components/ui/button";
import { buttonVariants } from "@/components/ui/buttonVariants";
import { registerForWorkshop } from "@/services/contentApi";
import { getRequestErrorMessage } from "@/services/requestError";
import type { Workshop } from "@/types/content";
import { formatDate } from "@/utils/format";

interface WorkshopCardProps {
  workshop: Workshop;
}

export default function WorkshopCard({ workshop }: WorkshopCardProps) {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [feedback, setFeedback] = useState<{ type: "success" | "error"; text: string } | null>(null);
  const isAuthenticated = Boolean(localStorage.getItem("hssa_access_token") ?? sessionStorage.getItem("hssa_access_token"));

  async function handleRegister() {
    setIsSubmitting(true);
    setFeedback(null);
    try {
      await registerForWorkshop(workshop.id);
      setFeedback({ type: "success", text: "Registration confirmed." });
    } catch (error) {
      setFeedback({ type: "error", text: getRequestErrorMessage(error, "Registration could not be completed.") });
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <article className="flex h-full flex-col overflow-hidden rounded-2xl border border-slate-200 bg-white card-lift">
      {workshop.imageUrl ? <img src={workshop.imageUrl} alt={`${workshop.title} card`} className="aspect-video w-full object-cover" /> : null}
      <div className="flex flex-1 flex-col p-6">
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
        {workshop.seats > 0 ? <div className="flex items-center gap-2">
          <Users className="size-4 text-slate-500" />
          <span>{workshop.seats} seats</span>
        </div> : null}
      </dl>
      <div className="mt-6 flex items-center justify-between gap-4 border-t border-slate-100 pt-5">
        <span className="text-sm font-semibold text-slate-700">{workshop.priceLabel}</span>
        {isAuthenticated ? <Button type="button" size="sm" onClick={handleRegister} disabled={workshop.status === "Past" || isSubmitting || feedback?.type === "success"} className="bg-blue-600 hover:bg-blue-700">{isSubmitting ? "Registering..." : feedback?.type === "success" ? "Registered" : "Register"}</Button> : <Link to="/login" aria-disabled={workshop.status === "Past"} className={buttonVariants({ variant: "outline", size: "sm", className: workshop.status === "Past" ? "pointer-events-none opacity-50" : "" })}>Login to Register</Link>}
      </div>
      {feedback ? <p role="status" className={`mt-3 text-xs font-semibold ${feedback.type === "success" ? "text-emerald-700" : "text-red-700"}`}>{feedback.text}</p> : null}
      </div>
    </article>
  );
}
