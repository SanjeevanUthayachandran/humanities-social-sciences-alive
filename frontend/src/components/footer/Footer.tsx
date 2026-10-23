import { Globe, Mail, MapPin, Phone, Share2, Video } from "lucide-react";
import { useState, type FormEvent } from "react";
import { Link } from "react-router-dom";
import brandImage from "@/assets/hssa-brand.png";
import Container from "@/components/common/Container";
import { Button } from "@/components/ui/button";
import { NAVIGATION } from "@/constants/navigation";
import { subscribeToNewsletter } from "@/services/contactService";
import { getRequestErrorMessage } from "@/services/requestError";

export default function Footer() {
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
      setFeedback("Subscribed successfully.");
    } catch (error) {
      setFeedback(getRequestErrorMessage(error, "Subscription failed. Please try again."));
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <footer className="border-t border-slate-200 bg-slate-950 text-white">
      <Container>
        <div className="grid gap-10 py-12 lg:grid-cols-[1.3fr_0.7fr_0.8fr_1fr]">
          <div>
            <Link to="/" className="flex items-center gap-3">
              <img src={brandImage} alt="" className="size-12 rounded-md object-cover object-left" />
              <span className="text-lg font-bold leading-tight">
                Humanities & Social
                <span className="block text-emerald-300">Sciences Alive</span>
              </span>
            </Link>
            <p className="mt-5 max-w-md text-sm leading-6 text-slate-300">
              A professional digital education and research platform for critical thinking,
              humanities learning, social science inquiry, and public intellectual life.
            </p>
            <div className="mt-6 flex gap-2">
              {[
                { label: "Social page", icon: Share2 },
                { label: "Professional network", icon: Globe },
                { label: "Video channel", icon: Video },
              ].map(({ label, icon: Icon }) => (
                <a
                  key={label}
                  href="#"
                  aria-label={label}
                  className="grid size-10 place-items-center rounded-md border border-white/10 bg-white/5 text-slate-200 transition hover:bg-white/10 hover:text-white"
                >
                  <Icon className="size-4" />
                </a>
              ))}
            </div>
          </div>

          <div>
            <h2 className="text-sm font-bold uppercase tracking-[0.16em] text-slate-300">Quick Links</h2>
            <ul className="mt-5 space-y-3">
              {NAVIGATION.slice(0, 6).map((item) => (
                <li key={item.path}>
                  <Link to={item.path} className="text-sm text-slate-300 transition hover:text-white">
                    {item.name}
                  </Link>
                </li>
              ))}
            </ul>
          </div>

          <div>
            <h2 className="text-sm font-bold uppercase tracking-[0.16em] text-slate-300">Contact</h2>
            <ul className="mt-5 space-y-4 text-sm text-slate-300">
              <li className="flex gap-3">
                <Mail className="size-4 text-emerald-300" />
                hello@hssa.edu
              </li>
              <li className="flex gap-3">
                <Phone className="size-4 text-emerald-300" />
                +94 77 000 0000
              </li>
              <li className="flex gap-3">
                <MapPin className="size-4 text-emerald-300" />
                Digital-first, Sri Lanka
              </li>
            </ul>
          </div>

          <div>
            <h2 className="text-sm font-bold uppercase tracking-[0.16em] text-slate-300">Newsletter</h2>
            <p className="mt-5 text-sm leading-6 text-slate-300">
              Receive course launches, research updates, and workshop announcements.
            </p>
            <form onSubmit={handleNewsletterSubmit} className="mt-5 flex flex-col gap-3" aria-label="Newsletter signup">
              <label htmlFor="newsletter-email" className="sr-only">
                Email address
              </label>
              <input
                id="newsletter-email"
                name="email"
                required
                type="email"
                placeholder="Email address"
                className="h-11 rounded-md border border-white/10 bg-white/10 px-4 text-sm text-white outline-none placeholder:text-slate-400 focus:border-blue-300 focus:ring-3 focus:ring-blue-300/20"
              />
              <Button type="submit" disabled={isSubmitting} className="bg-blue-600 hover:bg-blue-700">
                {isSubmitting ? "Subscribing..." : "Subscribe"}
              </Button>
              {feedback ? <p role="status" className="text-xs text-slate-300">{feedback}</p> : null}
            </form>
          </div>
        </div>

        <div className="flex flex-col gap-3 border-t border-white/10 py-6 text-sm text-slate-400 sm:flex-row sm:items-center sm:justify-between">
          <p>Copyright 2026 Humanities & Social Sciences Alive. All rights reserved.</p>
          <div className="flex gap-4">
            <Link to="/login" className="transition hover:text-white">
              Login
            </Link>
            <Link to="/register" className="transition hover:text-white">
              Register
            </Link>
            <Link to="/search" className="transition hover:text-white">
              Search
            </Link>
          </div>
        </div>
      </Container>
    </footer>
  );
}
