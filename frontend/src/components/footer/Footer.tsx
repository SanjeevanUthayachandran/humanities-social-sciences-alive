import { Mail, MapPin, Phone } from "lucide-react";
import { useState, type FormEvent } from "react";
import { Link } from "react-router-dom";
import Container from "@/components/common/Container";
import { Button } from "@/components/ui/button";
import { useContent } from "@/hooks/useContent";
import { subscribeToNewsletter } from "@/services/contactService";
import { getRequestErrorMessage } from "@/services/requestError";

export default function Footer() {
  const { offices } = useContent();
  const office = offices.find((item) => item.isHeadOffice) ?? offices[0];
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [feedback, setFeedback] = useState<string | null>(null);

  async function handleNewsletterSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setIsSubmitting(true);
    setFeedback(null);
    const form = new FormData(event.currentTarget);
    try {
      await subscribeToNewsletter(String(form.get("email") ?? ""));
      event.currentTarget.reset();
      setFeedback("You are subscribed.");
    } catch (error) {
      setFeedback(getRequestErrorMessage(error, "Subscription failed. Please try again."));
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <footer className="bg-slate-950 text-white">
      <Container>
        <div className="grid gap-10 py-14 md:grid-cols-2 lg:grid-cols-[1.2fr_0.8fr_1fr] lg:gap-16">
          <div>
            <Link to="/" className="inline-flex items-center gap-3" aria-label="Humanities & Social Sciences Alive home">
              <img src="/images/hssa-logo.svg" alt="" aria-hidden="true" className="size-14 rounded-full ring-1 ring-white/20" />
              <span className="text-base font-bold leading-tight">Humanities & Social<span className="block text-blue-100">Sciences Alive</span></span>
            </Link>
            <p className="mt-5 max-w-sm text-sm leading-6 text-white/80">Subject knowledge, academic English, research practice, and public learning in one connected platform.</p>
          </div>
          <div>
            <h2 className="text-xs font-bold uppercase tracking-[0.18em] text-white/80">Contact</h2>
            {office ? <ul className="mt-5 space-y-4 text-sm text-white/80">
              {office.email ? <li><a href={`mailto:${office.email}`} className="flex gap-3 hover:text-white hover:underline underline-offset-4"><Mail className="mt-0.5 size-4 shrink-0 text-blue-100" />{office.email}</a></li> : null}
              {office.phone ? <li><a href={`tel:${office.phone}`} className="flex gap-3 hover:text-white hover:underline underline-offset-4"><Phone className="mt-0.5 size-4 shrink-0 text-blue-100" />{office.phone}</a></li> : null}
              {office.address ? <li className="flex gap-3"><MapPin className="mt-0.5 size-4 shrink-0 text-blue-100" />{office.address}</li> : null}
            </ul> : <p className="mt-5 text-sm leading-6 text-white/65">Contact details are managed through the platform.</p>}
          </div>
          <div>
            <h2 className="text-xs font-bold uppercase tracking-[0.18em] text-white/80">Newsletter</h2>
            <p className="mt-5 text-sm leading-6 text-white/80">Receive course, research, workshop, and event updates.</p>
            <form onSubmit={handleNewsletterSubmit} className="mt-5 flex flex-col gap-3" aria-label="Newsletter signup">
              <label htmlFor="newsletter-email" className="sr-only">Email address</label>
              <input id="newsletter-email" name="email" required type="email" autoComplete="email" placeholder="Email address" className="h-11 rounded-full border border-white/20 bg-white/10 px-4 text-sm text-white outline-none placeholder:text-white/60 focus:border-white focus:ring-3 focus:ring-white/20" />
              <Button type="submit" disabled={isSubmitting} className="bg-blue-600 text-white hover:bg-blue-700">{isSubmitting ? "Subscribing..." : "Subscribe"}</Button>
              {feedback ? <p role="status" className="text-xs text-white/80">{feedback}</p> : null}
            </form>
          </div>
        </div>
      </Container>
    </footer>
  );
}
