import { ArrowRight } from "lucide-react";
import { motion } from "framer-motion";
import { Link } from "react-router-dom";
import Container from "@/components/common/Container";
import { buttonVariants } from "@/components/ui/buttonVariants";

export default function Hero() {
  return (
    <section className="relative isolate flex min-h-[560px] items-center overflow-hidden bg-slate-950 py-20 text-white sm:min-h-[620px] lg:py-28">
      <img
        src="/images/hssa-campus-hero.png"
        alt=""
        aria-hidden="true"
        className="absolute inset-0 -z-30 size-full object-cover object-[center_58%]"
      />
      <div className="absolute inset-0 -z-20 bg-gradient-to-r from-slate-950/90 via-slate-950/60 to-slate-950/5" />
      <div className="absolute inset-x-0 bottom-0 -z-10 h-1/3 bg-gradient-to-t from-slate-950/50 to-transparent" />
      <Container>
        <motion.div
          className="max-w-2xl"
          initial={{ opacity: 0, y: 22 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6, ease: "easeOut" }}
        >
          <p className="text-xs font-bold uppercase tracking-[0.2em] text-blue-300">
            Humanities & Social Sciences Alive
          </p>
          <h1 className="mt-5 text-4xl font-semibold leading-[1.08] text-white sm:text-5xl lg:text-6xl">
            Learn your subject in English. Carry your ideas further.
          </h1>
          <p className="mt-6 max-w-xl text-base leading-7 text-slate-100/90 sm:text-lg sm:leading-8">
            Strengthen subject knowledge, academic English, research practice, and career
            confidence in one connected platform.
          </p>
          <div className="mt-8 flex flex-col gap-3 sm:flex-row">
            <Link to="/courses" className={buttonVariants({ size: "lg", className: "bg-blue-600 text-white hover:bg-blue-500" })}>
              Find a Course <ArrowRight />
            </Link>
            <Link to="/reading-club" className={buttonVariants({ variant: "outline", size: "lg", className: "border-white/40 bg-transparent text-white hover:bg-white/10 hover:text-white" })}>
              Join the Reading Journey
            </Link>
          </div>
        </motion.div>
      </Container>
    </section>
  );
}
