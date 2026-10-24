import { CheckCircle2, Mail, ShieldCheck } from "lucide-react";
import { useState, type FormEvent } from "react";
import { Link } from "react-router-dom";
import Container from "@/components/common/Container";
import { Button } from "@/components/ui/button";
import { requestPasswordReset } from "@/services/authService";
import { getRequestErrorMessage } from "@/services/requestError";

export default function ForgotPasswordPage() {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [feedback, setFeedback] = useState<{ type: "success" | "error"; text: string } | null>(null);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setIsSubmitting(true);
    setFeedback(null);
    const form = new FormData(event.currentTarget);
    try {
      const message = await requestPasswordReset(String(form.get("email") ?? ""));
      setFeedback({ type: "success", text: message });
    } catch (error) {
      setFeedback({ type: "error", text: getRequestErrorMessage(error, "The reset request could not be sent.") });
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <section className="bg-slate-50 px-4 py-12 sm:py-20">
      <Container className="max-w-2xl">
        <form onSubmit={handleSubmit} className="rounded-2xl border border-slate-200 bg-white p-6 shadow-[0_24px_60px_-32px_rgba(15,23,42,0.35)] sm:p-10">
          <div className="text-center">
            <span className="mx-auto flex size-16 items-center justify-center rounded-full bg-blue-50 text-blue-700 ring-1 ring-blue-100"><ShieldCheck className="size-7" aria-hidden="true" /></span>
            <p className="mt-5 text-xs font-bold uppercase tracking-[0.2em] text-blue-700">Account recovery</p>
            <h1 className="mt-3 text-3xl font-bold tracking-tight text-slate-950">Reset your password</h1>
            <p className="mt-2 text-sm leading-6 text-slate-600">Enter your account email. We will send a secure, one-time link that expires after 30 minutes.</p>
          </div>
          {feedback ? (
            <div role="status" className={`mt-7 flex items-start gap-3 rounded-lg border px-4 py-3 text-sm ${feedback.type === "success" ? "border-emerald-200 bg-emerald-50 text-emerald-900" : "border-red-200 bg-red-50 font-semibold text-red-700"}`}>
              {feedback.type === "success" ? <CheckCircle2 className="mt-0.5 size-5 shrink-0 text-emerald-700" aria-hidden="true" /> : null}
              <div>
                <p className="font-semibold">{feedback.text}</p>
                {feedback.type === "success" ? (
                  <p className="mt-2 font-normal leading-5 text-emerald-800">If it does not arrive within a few minutes, check Spam and confirm that you previously registered an HSSA account using this exact email address.</p>
                ) : null}
              </div>
            </div>
          ) : null}
          <label className="mt-8 block">
            <span className="text-sm font-semibold text-slate-700">Email address</span>
            <span className="relative mt-2 block">
              <Mail className="pointer-events-none absolute left-4 top-1/2 size-5 -translate-y-1/2 text-slate-400" />
              <input name="email" required type="email" autoComplete="email" className="h-12 w-full rounded-lg border border-slate-200 pl-12 pr-4 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
            </span>
          </label>
          <Button type="submit" disabled={isSubmitting} className="mt-6 h-12 w-full bg-blue-600 hover:bg-blue-700">{isSubmitting ? "Sending secure link..." : "Email me a reset link"}</Button>
          <p className="mt-4 text-center text-xs leading-5 text-slate-500">For your security, we show the same confirmation whether or not an account exists.</p>
          <p className="mt-6 text-center text-sm text-slate-600"><Link to="/login" className="font-semibold text-blue-700 hover:text-blue-800">Back to login</Link></p>
        </form>
      </Container>
    </section>
  );
}
