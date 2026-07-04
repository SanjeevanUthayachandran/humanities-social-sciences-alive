import { Check, Globe, Lock, Mail, UserRound } from "lucide-react";
import { Link } from "react-router-dom";
import Container from "@/components/common/Container";
import PageBanner from "@/components/common/PageBanner";
import { Button } from "@/components/ui/button";

export default function RegisterPage() {
  return (
    <>
      <PageBanner
        eyebrow="Register"
        title="Create your future learner account."
        description="A production-style registration UI with validation states, password strength, terms checkbox, and Google login placeholder."
      />

      <section className="py-16 sm:py-20">
        <Container className="max-w-4xl">
          <form className="rounded-lg border border-slate-200 bg-white p-6 shadow-xl">
            <div className="grid gap-5 sm:grid-cols-2">
              <label className="block">
                <span className="text-sm font-semibold text-slate-700">First name</span>
                <span className="relative mt-2 block">
                  <UserRound className="pointer-events-none absolute left-4 top-1/2 size-5 -translate-y-1/2 text-slate-400" />
                  <input className="h-12 w-full rounded-md border border-slate-200 pl-12 pr-4 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
                </span>
              </label>
              <label className="block">
                <span className="text-sm font-semibold text-slate-700">Last name</span>
                <input className="mt-2 h-12 w-full rounded-md border border-slate-200 px-4 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
              </label>
              <label className="block sm:col-span-2">
                <span className="text-sm font-semibold text-slate-700">Email</span>
                <span className="relative mt-2 block">
                  <Mail className="pointer-events-none absolute left-4 top-1/2 size-5 -translate-y-1/2 text-slate-400" />
                  <input type="email" aria-invalid="false" className="h-12 w-full rounded-md border border-emerald-300 pl-12 pr-12 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" defaultValue="learner@example.com" />
                  <Check className="pointer-events-none absolute right-4 top-1/2 size-5 -translate-y-1/2 text-emerald-600" />
                </span>
                <p className="mt-2 text-xs font-medium text-emerald-700">Email format looks valid.</p>
              </label>
              <label className="block sm:col-span-2">
                <span className="text-sm font-semibold text-slate-700">Password</span>
                <span className="relative mt-2 block">
                  <Lock className="pointer-events-none absolute left-4 top-1/2 size-5 -translate-y-1/2 text-slate-400" />
                  <input type="password" className="h-12 w-full rounded-md border border-slate-200 pl-12 pr-4 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" defaultValue="Humanities2026" />
                </span>
                <div className="mt-3 grid grid-cols-4 gap-2" aria-label="Password strength: strong">
                  {[0, 1, 2, 3].map((item) => (
                    <span key={item} className="h-2 rounded-full bg-emerald-500" />
                  ))}
                </div>
                <p className="mt-2 text-xs font-medium text-emerald-700">Strong password.</p>
              </label>
              <label className="flex gap-3 sm:col-span-2">
                <input type="checkbox" className="mt-1 size-4 rounded border-slate-300 text-blue-600 focus:ring-blue-500" />
                <span className="text-sm leading-6 text-slate-600">
                  I agree to the terms, privacy policy, and future learning portal conditions.
                </span>
              </label>
            </div>
            <div className="mt-6 grid gap-3 sm:grid-cols-2">
              <Button type="button" className="h-12 bg-blue-600 hover:bg-blue-700">
                Create Account
              </Button>
              <Button type="button" variant="outline" className="h-12">
                <Globe />
                Sign up with Google
              </Button>
            </div>
            <p className="mt-6 text-center text-sm text-slate-600">
              Already have an account?{" "}
              <Link to="/login" className="font-semibold text-blue-700 hover:text-blue-800">
                Login
              </Link>
            </p>
          </form>
        </Container>
      </section>
    </>
  );
}
