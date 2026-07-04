import { Globe, Mail, MapPin, Phone, Send, Share2, Video } from "lucide-react";
import FaqList from "@/components/contact/FaqList";
import Container from "@/components/common/Container";
import PageBanner from "@/components/common/PageBanner";
import SectionHeader from "@/components/common/SectionHeader";
import { Button } from "@/components/ui/button";
import { faqs } from "@/services/contentService";

const contactMethods = [
  { label: "Email", value: "hello@hssa.edu", icon: Mail },
  { label: "Phone", value: "+94 77 000 0000", icon: Phone },
  { label: "Location", value: "Digital-first, Sri Lanka", icon: MapPin },
];

export default function ContactPage() {
  return (
    <>
      <PageBanner
        eyebrow="Contact"
        title="Speak with the Humanities & Social Sciences Alive team."
        description="Use the UI-only contact form for presentation now. It is ready for email, CRM, or backend message handling later."
      />

      <section className="py-16 sm:py-20">
        <Container>
          <div className="grid gap-8 lg:grid-cols-[1fr_0.85fr]">
            <form className="rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
              <SectionHeader
                eyebrow="Message"
                title="Send an inquiry."
                description="Ask about courses, research, workshops, events, or future collaboration."
                className="mb-8"
              />
              <div className="grid gap-5 sm:grid-cols-2">
                <label className="block">
                  <span className="text-sm font-semibold text-slate-700">First name</span>
                  <input className="mt-2 h-11 w-full rounded-md border border-slate-200 px-4 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
                </label>
                <label className="block">
                  <span className="text-sm font-semibold text-slate-700">Last name</span>
                  <input className="mt-2 h-11 w-full rounded-md border border-slate-200 px-4 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
                </label>
                <label className="block sm:col-span-2">
                  <span className="text-sm font-semibold text-slate-700">Email</span>
                  <input type="email" className="mt-2 h-11 w-full rounded-md border border-slate-200 px-4 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
                </label>
                <label className="block sm:col-span-2">
                  <span className="text-sm font-semibold text-slate-700">Topic</span>
                  <select className="mt-2 h-11 w-full rounded-md border border-slate-200 px-4 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100">
                    <option>Courses</option>
                    <option>Research</option>
                    <option>Workshops</option>
                    <option>Events</option>
                    <option>Collaboration</option>
                  </select>
                </label>
                <label className="block sm:col-span-2">
                  <span className="text-sm font-semibold text-slate-700">Message</span>
                  <textarea className="mt-2 min-h-36 w-full rounded-md border border-slate-200 px-4 py-3 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
                </label>
              </div>
              <Button type="button" className="mt-6 bg-blue-600 hover:bg-blue-700">
                Send Message
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
