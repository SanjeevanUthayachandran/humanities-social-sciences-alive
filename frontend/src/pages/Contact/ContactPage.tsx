import { Clock3, ExternalLink, Mail, MapPin, Phone, Send } from "lucide-react";
import { useState, type FormEvent } from "react";
import FaqList from "@/components/contact/FaqList";
import Container from "@/components/common/Container";
import EmptyState from "@/components/common/EmptyState";
import PageBanner from "@/components/common/PageBanner";
import SectionHeader from "@/components/common/SectionHeader";
import { Button } from "@/components/ui/button";
import { useContent } from "@/hooks/useContent";
import { sendContactMessage } from "@/services/contactService";
import { getRequestErrorMessage } from "@/services/requestError";

export default function ContactPage() {
  const { faqs, offices, isLoading, failedResources } = useContent();
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
      <PageBanner eyebrow="Contact" title="Start a conversation with HSSA." description="Ask about courses, research, workshops, events, partnerships, or accessibility. Your message goes directly to the contact service." />

      <section className="py-16 sm:py-20">
        <Container>
          <div className="grid gap-8 lg:grid-cols-[1fr_0.85fr]">
            <form onSubmit={handleSubmit} className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm sm:p-8">
              <SectionHeader eyebrow="Send a message" title="How can we help?" description="Share enough detail for the right person to respond." className="mb-8" />
              <div className="grid gap-5 sm:grid-cols-2">
                <label className="block"><span className="text-sm font-semibold text-slate-700">First name</span><input name="firstName" required autoComplete="given-name" className="mt-2 h-11 w-full rounded-lg border border-slate-200 px-4 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" /></label>
                <label className="block"><span className="text-sm font-semibold text-slate-700">Last name</span><input name="lastName" required autoComplete="family-name" className="mt-2 h-11 w-full rounded-lg border border-slate-200 px-4 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" /></label>
                <label className="block sm:col-span-2"><span className="text-sm font-semibold text-slate-700">Email</span><input name="email" required type="email" autoComplete="email" className="mt-2 h-11 w-full rounded-lg border border-slate-200 px-4 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" /></label>
                <label className="block sm:col-span-2"><span className="text-sm font-semibold text-slate-700">Topic</span><select name="inquiryType" className="mt-2 h-11 w-full rounded-lg border border-slate-200 px-4 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100"><option>Courses</option><option>Research</option><option>Workshops</option><option>Events</option><option>Collaboration</option><option>General</option></select></label>
                <label className="block sm:col-span-2"><span className="text-sm font-semibold text-slate-700">Message</span><textarea name="message" required className="mt-2 min-h-36 w-full rounded-lg border border-slate-200 px-4 py-3 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" /></label>
              </div>
              {feedback ? <p role="status" className={`mt-5 text-sm font-semibold ${feedback.type === "success" ? "text-emerald-700" : "text-red-700"}`}>{feedback.text}</p> : null}
              <Button type="submit" disabled={isSubmitting} className="mt-6 bg-blue-600 hover:bg-blue-700">{isSubmitting ? "Sending..." : "Send Message"}<Send /></Button>
            </form>

            <div>
              <SectionHeader eyebrow="Contact details" title="Reach the right office." description="These details are managed in the HSSA administration system." className="mb-6" />
              {offices.length ? <div className="grid gap-5">{offices.map((office) => (
                <article key={office.id} className="rounded-2xl border border-slate-200 bg-slate-50 p-6 card-lift">
                  <div className="flex items-start justify-between gap-4"><h2 className="text-xl font-bold text-slate-950">{office.officeName}</h2>{office.isHeadOffice ? <span className="rounded-full bg-blue-100 px-3 py-1 text-xs font-bold text-blue-700">Head office</span> : null}</div>
                  <div className="mt-5 space-y-4 text-sm text-slate-600">
                    {office.email ? <a href={`mailto:${office.email}`} className="flex gap-3 hover:text-blue-700"><Mail className="mt-0.5 size-4 shrink-0 text-blue-600" />{office.email}</a> : null}
                    {office.phone ? <a href={`tel:${office.phone}`} className="flex gap-3 hover:text-blue-700"><Phone className="mt-0.5 size-4 shrink-0 text-emerald-600" />{office.phone}</a> : null}
                    {office.address ? <p className="flex gap-3"><MapPin className="mt-0.5 size-4 shrink-0 text-blue-600" />{office.address}</p> : null}
                    {office.openingHours ? <p className="flex gap-3"><Clock3 className="mt-0.5 size-4 shrink-0 text-emerald-600" />{office.openingHours}</p> : null}
                  </div>
                  {office.googleMapUrl ? <a href={office.googleMapUrl} target="_blank" rel="noreferrer" className="mt-5 inline-flex items-center gap-2 text-sm font-semibold text-blue-700 hover:text-blue-800">Open map <ExternalLink className="size-4" /></a> : null}
                </article>
              ))}</div> : <EmptyState title={isLoading ? "Loading contact details" : "Contact details unavailable"} description={failedResources.includes("offices") ? "Contact details could not be loaded. You can still send us a message using this form." : "You can still send a message using this form."} />}
            </div>
          </div>
        </Container>
      </section>

      <section className="bg-slate-100/70 py-16 sm:py-20">
        <Container>
          <SectionHeader eyebrow="FAQ" title="Answers to common questions." description="Quick answers to what learners ask us most." />
          {faqs.length ? <FaqList items={faqs} /> : <EmptyState title={isLoading ? "Loading answers" : "No FAQs published"} description="Send us a message and the team will help." />}
        </Container>
      </section>
    </>
  );
}
