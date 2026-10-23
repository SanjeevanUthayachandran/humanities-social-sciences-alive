import { Check, Globe, Lock, Mail, UserRound } from "lucide-react";
import { useState, type FormEvent } from "react";
import { Link } from "react-router-dom";
import Container from "@/components/common/Container";
import PageBanner from "@/components/common/PageBanner";
import { Button } from "@/components/ui/button";
import { register } from "@/services/authService";
import { getRequestErrorMessage } from "@/services/requestError";

export default function RegisterPage() {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [feedback, setFeedback] = useState<{ type: "success" | "error"; text: string } | null>(null);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setIsSubmitting(true);
    setFeedback(null);
    const form = new FormData(event.currentTarget);

    try {
      const message = await register({
        firstName: String(form.get("firstName") ?? ""),
        lastName: String(form.get("lastName") ?? ""),
        email: String(form.get("email") ?? ""),
        password: String(form.get("password") ?? ""),
      });
      event.currentTarget.reset();
      setFeedback({ type: "success", text: message });
    } catch (requestError) {
      setFeedback({ type: "error", text: getRequestErrorMessage(requestError, "Registration failed.") });
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <>
      <PageBanner
        eyebrow="Register"
        title="Create your future learner account."
        description="Create an HSSA account to access learning and community features."
      />

      <section className="py-16 sm:py-20">
        <Container className="max-w-4xl">
          <form onSubmit={handleSubmit} className="rounded-lg border border-slate-200 bg-white p-6 shadow-xl">
            <div className="grid gap-5 sm:grid-cols-2">
              <label className="block">
                <span className="text-sm font-semibold text-slate-700">First name</span>
                <span className="relative mt-2 block">
                  <UserRound className="pointer-events-none absolute left-4 top-1/2 size-5 -translate-y-1/2 text-slate-400" />
                  <input name="firstName" required autoComplete="given-name" className="h-12 w-full rounded-md border border-slate-200 pl-12 pr-4 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
                </span>
              </label>
              <label className="block">
                <span className="text-sm font-semibold text-slate-700">Last name</span>
                <input name="lastName" required autoComplete="family-name" className="mt-2 h-12 w-full rounded-md border border-slate-200 px-4 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
              </label>
              <label className="block sm:col-span-2">
                <span className="text-sm font-semibold text-slate-700">Email</span>
                <span className="relative mt-2 block">
                  <Mail className="pointer-events-none absolute left-4 top-1/2 size-5 -translate-y-1/2 text-slate-400" />
                  <input name="email" required type="email" autoComplete="email" aria-invalid="false" className="h-12 w-full rounded-md border border-emerald-300 pl-12 pr-12 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
                  <Check className="pointer-events-none absolute right-4 top-1/2 size-5 -translate-y-1/2 text-emerald-600" />
                </span>
                <p className="mt-2 text-xs font-medium text-emerald-700">Email format looks valid.</p>
              </label>
              <label className="block sm:col-span-2">
                <span className="text-sm font-semibold text-slate-700">Password</span>
                <span className="relative mt-2 block">
                  <Lock className="pointer-events-none absolute left-4 top-1/2 size-5 -translate-y-1/2 text-slate-400" />
                  <input name="password" required minLength={8} type="password" autoComplete="new-password" className="h-12 w-full rounded-md border border-slate-200 pl-12 pr-4 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
                </span>
                <div className="mt-3 grid grid-cols-4 gap-2" aria-label="Password strength: strong">
                  {[0, 1, 2, 3].map((item) => (
                    <span key={item} className="h-2 rounded-full bg-emerald-500" />
                  ))}
                </div>
                <p className="mt-2 text-xs font-medium text-emerald-700">Strong password.</p>
              </label>
              <label className="flex gap-3 sm:col-span-2">
                <input required type="checkbox" className="mt-1 size-4 rounded border-slate-300 text-blue-600 focus:ring-blue-500" />
                <span className="text-sm leading-6 text-slate-600">
                  I agree to the terms, privacy policy, and future learning portal conditions.
                </span>
              </label>
            </div>
            {feedback ? <p role="status" className={`mt-6 text-sm font-semibold ${feedback.type === "success" ? "text-emerald-700" : "text-red-700"}`}>{feedback.text}</p> : null}
            <div className="mt-6 grid gap-3 sm:grid-cols-2">
              <Button type="submit" disabled={isSubmitting} className="h-12 bg-blue-600 hover:bg-blue-700">
                {isSubmitting ? "Creating account..." : "Create Account"}
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
