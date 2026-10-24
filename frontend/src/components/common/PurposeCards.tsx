import { Eye, Flag, Lightbulb } from "lucide-react";
import Reveal from "@/components/common/Reveal";
import { cn } from "@/lib/utils";

const ITEMS = [
  {
    icon: Lightbulb,
    label: "Our purpose",
    title: "Keep the humanities and social sciences alive.",
    text: "An online platform of courses, workshops, collective research, and new initiatives that foster creativity and critical thinking, and support prospective learners in these disciplines.",
    tone: "bg-blue-600",
  },
  {
    icon: Eye,
    label: "Our vision",
    title: "Understand life, stay creative and critical.",
    text: "Helping people understand life\u2019s ultimate truth, birth and death, while being creative and critical of everything around us.",
    tone: "bg-emerald-600",
  },
  {
    icon: Flag,
    label: "Our mission",
    title: "Be in the game. Do the right thing.",
    text: "Making people aware of emerging global dynamics and the importance of doing the right thing in pursuit of social justice.",
    tone: "bg-slate-950",
  },
];

export default function PurposeCards({ className }: { className?: string }) {
  return (
    <div className={cn("grid gap-5 md:grid-cols-3", className)}>
      {ITEMS.map(({ icon: Icon, label, title, text, tone }, index) => (
        <Reveal key={label} delay={index * 0.06} className="h-full">
          <article className="flex h-full flex-col rounded-3xl border border-slate-200 bg-white p-7 card-lift">
            <span className={cn("flex size-12 items-center justify-center rounded-2xl text-white", tone)}>
              <Icon className="size-6" aria-hidden="true" />
            </span>
            <p className="mt-6 text-xs font-bold uppercase tracking-[0.18em] text-blue-600">{label}</p>
            <h3 className="mt-2 text-xl font-semibold leading-snug text-slate-950">{title}</h3>
            <p className="mt-3 text-sm leading-6 text-slate-600">{text}</p>
          </article>
        </Reveal>
      ))}
    </div>
  );
}
