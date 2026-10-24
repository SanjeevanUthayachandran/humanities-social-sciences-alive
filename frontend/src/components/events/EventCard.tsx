import { CalendarDays, MapPin } from "lucide-react";
import { useState } from "react";
import { Link } from "react-router-dom";
import Badge from "@/components/common/Badge";
import { Button } from "@/components/ui/button";
import { buttonVariants } from "@/components/ui/buttonVariants";
import { registerForEvent } from "@/services/contentApi";
import { getRequestErrorMessage } from "@/services/requestError";
import type { EventItem } from "@/types/content";
import { formatDate } from "@/utils/format";

interface EventCardProps {
  event: EventItem;
}

export default function EventCard({ event }: EventCardProps) {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [feedback, setFeedback] = useState<{ type: "success" | "error"; text: string } | null>(null);
  const isAuthenticated = Boolean(localStorage.getItem("hssa_access_token") ?? sessionStorage.getItem("hssa_access_token"));

  async function handleRegister() {
    setIsSubmitting(true);
    setFeedback(null);
    try {
      await registerForEvent(event.id);
      setFeedback({ type: "success", text: "Registration confirmed." });
    } catch (error) {
      setFeedback({ type: "error", text: getRequestErrorMessage(error, "Registration could not be completed.") });
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <article className="overflow-hidden rounded-2xl border border-slate-200 bg-white card-lift">
      {event.imageUrl ? <img src={event.imageUrl} alt={`${event.title} card`} className="aspect-video w-full object-cover" /> : null}
      <div className="p-6">
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
      <div className="mt-6 border-t border-slate-100 pt-5">
        {isAuthenticated ? <Button type="button" size="sm" onClick={handleRegister} disabled={isSubmitting || feedback?.type === "success"} className="bg-blue-600 hover:bg-blue-700">{isSubmitting ? "Registering..." : feedback?.type === "success" ? "Registered" : "Register for Event"}</Button> : <Link to="/login" className={buttonVariants({ variant: "outline", size: "sm" })}>Login to Register</Link>}
        {feedback ? <p role="status" className={`mt-3 text-xs font-semibold ${feedback.type === "success" ? "text-emerald-700" : "text-red-700"}`}>{feedback.text}</p> : null}
      </div>
      </div>
    </article>
  );
}
