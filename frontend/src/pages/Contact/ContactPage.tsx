import { Globe, Mail, MapPin, Phone, Send, Share2, Video } from "lucide-react";
import { useState, type FormEvent } from "react";
import FaqList from "@/components/contact/FaqList";
import Container from "@/components/common/Container";
import PageBanner from "@/components/common/PageBanner";
import SectionHeader from "@/components/common/SectionHeader";
import { Button } from "@/components/ui/button";
import { useContent } from "@/hooks/useContent";
import { sendContactMessage } from "@/services/contactService";
import { getRequestErrorMessage } from "@/services/requestError";

const contactMethods = [
  { label: "Email", value: "hello@hssa.edu", icon: Mail },
  { label: "Phone", value: "+94 77 000 0000", icon: Phone },
  { label: "Location", value: "Digital-first, Sri Lanka", icon: MapPin },
];

export default function ContactPage() {
  const { faqs } = useContent();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [feedback, setFeedback] = useState<{ type: "success" | "error"; text: string } | null>(null);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setIsSubmitting(true);
    setFeedback(null);
    const form = new FormData(event.currentTarget);

    try {
      await sendContactMessage({
        fullName: `${form.get("firstName") ?? ""} ${form.get("lastName") ?? ""}`.trim(),
        email: String(form.get("email") ?? ""),
        subject: `${form.get("inquiryType") ?? "General"} inquiry`,
        inquiryType: String(form.get("inquiryType") ?? "General"),
        message: String(form.get("message") ?? ""),
      });
      event.currentTarget.reset();
      setFeedback({ type: "success", text: "Your message was sent successfully." });
    } catch (error) {
      setFeedback({ type: "error", text: getRequestErrorMessage(error, "Your message could not be sent.") });
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <>
      <PageBanner
        eyebrow="Contact"
        title="Speak with the Humanities & Social Sciences Alive team."
        description="Send an inquiry directly to the Humanities & Social Sciences Alive team."
      />

      <section className="py-16 sm:py-20">
        <Container>
          <div className="grid gap-8 lg:grid-cols-[1fr_0.85fr]">
            <form onSubmit={handleSubmit} className="rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
              <SectionHeader
                eyebrow="Message"
                title="Send an inquiry."
                description="Ask about courses, research, workshops, events, or future collaboration."
                className="mb-8"
              />
              <div className="grid gap-5 sm:grid-cols-2">
                <label className="block">
                  <span className="text-sm font-semibold text-slate-700">First name</span>
                  <input name="firstName" required className="mt-2 h-11 w-full rounded-md border border-slate-200 px-4 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
                </label>
                <label className="block">
                  <span className="text-sm font-semibold text-slate-700">Last name</span>
                  <input name="lastName" required className="mt-2 h-11 w-full rounded-md border border-slate-200 px-4 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
                </label>
                <label className="block sm:col-span-2">
                  <span className="text-sm font-semibold text-slate-700">Email</span>
                  <input name="email" required type="email" className="mt-2 h-11 w-full rounded-md border border-slate-200 px-4 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
                </label>
                <label className="block sm:col-span-2">
                  <span className="text-sm font-semibold text-slate-700">Topic</span>
                  <select name="inquiryType" className="mt-2 h-11 w-full rounded-md border border-slate-200 px-4 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100">
                    <option>Courses</option>
                    <option>Research</option>
                    <option>Workshops</option>
                    <option>Events</option>
                    <option>Collaboration</option>
                  </select>
                </label>
                <label className="block sm:col-span-2">
                  <span className="text-sm font-semibold text-slate-700">Message</span>
                  <textarea name="message" required className="mt-2 min-h-36 w-full rounded-md border border-slate-200 px-4 py-3 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
                </label>
              </div>
              {feedback ? (
                <p role="status" className={`mt-5 text-sm font-semibold ${feedback.type === "success" ? "text-emerald-700" : "text-red-700"}`}>
                  {feedback.text}
                </p>
              ) : null}
              <Button type="submit" disabled={isSubmitting} className="mt-6 bg-blue-600 hover:bg-blue-700">
                {isSubmitting ? "Sending..." : "Send Message"}
                <Send />
              </Button>
            </form>

            <div className="space-y-5">
              <article className="rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
                <h2 className="text-xl font-bold text-slate-950">Contact details</h2>
                <div className="mt-5 space-y-4">
                  {contactMethods.map(({ label, value, icon: Icon }) => (
                    <div key={label} className="flex gap-3">
                      <Icon className="mt-0.5 size-5 text-blue-600" />
                      <div>
                        <p className="text-sm font-semibold text-slate-950">{label}</p>
                        <p className="mt-1 text-sm text-slate-600">{value}</p>
                      </div>
                    </div>
                  ))}
                </div>
              </article>

              <article className="rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
                <h2 className="text-xl font-bold text-slate-950">Social media</h2>
                <div className="mt-5 flex gap-2">
                  {[
                    { label: "Social page", icon: Share2 },
                    { label: "Professional network", icon: Globe },
                    { label: "Video channel", icon: Video },
                  ].map(({ label, icon: Icon }) => (
                    <a
                      key={label}
                      href="#"
                      aria-label={label}
                      className="grid size-10 place-items-center rounded-md border border-slate-200 text-slate-600 transition hover:border-blue-200 hover:bg-blue-50 hover:text-blue-700"
                    >
                      <Icon className="size-4" />
                    </a>
                  ))}
                </div>
              </article>

              <article className="min-h-72 rounded-lg border border-slate-200 bg-slate-100 p-6 shadow-sm">
                <MapPin className="size-7 text-blue-600" />
                <h2 className="mt-4 text-xl font-bold text-slate-950">Google Map placeholder</h2>
                <p className="mt-3 text-sm leading-6 text-slate-600">
                  Map embed will be connected when the public address and production API keys are ready.
                </p>
              </article>
            </div>
          </div>
        </Container>
      </section>

      <section className="bg-slate-50 py-16 sm:py-20">
        <Container>
          <SectionHeader
            eyebrow="FAQ"
            title="Common questions."
            description="These answers keep the presentation transparent about what is UI-only and what is ready for backend integration."
          />
          <FaqList items={faqs} />
        </Container>
      </section>
    </>
  );
}
