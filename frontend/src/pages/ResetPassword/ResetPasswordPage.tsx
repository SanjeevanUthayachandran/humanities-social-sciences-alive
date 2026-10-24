import { CheckCircle2, Eye, EyeOff, LockKeyhole } from "lucide-react";
import { useState, type FormEvent } from "react";
import { Link, useSearchParams } from "react-router-dom";
import Container from "@/components/common/Container";
import { Button } from "@/components/ui/button";
import { resetPassword } from "@/services/authService";
import { getRequestErrorMessage } from "@/services/requestError";

export default function ResetPasswordPage() {
  const [params] = useSearchParams();
  const token = params.get("token") ?? "";
  const [busy, setBusy] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmation, setShowConfirmation] = useState(false);
  const [feedback, setFeedback] = useState<{ type: "success" | "error"; text: string } | null>(null);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const form = new FormData(event.currentTarget);
    const password = String(form.get("password") ?? "");
    const confirmation = String(form.get("confirmation") ?? "");
    if (password !== confirmation) {
      setFeedback({ type: "error", text: "The passwords do not match." });
      return;
    }
    setBusy(true);
    try {
      setFeedback({ type: "success", text: await resetPassword(token, password) });
    } catch (error) {
      setFeedback({ type: "error", text: getRequestErrorMessage(error, "The reset link could not be used.") });
    } finally {
      setBusy(false);
    }
  }

  const completed = feedback?.type === "success";

  return (
    <section className="bg-slate-50 px-4 py-12 sm:py-20">
      <Container className="max-w-2xl">
        <form onSubmit={handleSubmit} className="rounded-2xl border border-slate-200 bg-white p-6 shadow-[0_24px_60px_-32px_rgba(15,23,42,0.35)] sm:p-10">
          <div className="text-center">
            <span className="mx-auto flex size-16 items-center justify-center rounded-full bg-blue-50 text-blue-700 ring-1 ring-blue-100"><LockKeyhole className="size-7" aria-hidden="true" /></span>
            <p className="mt-5 text-xs font-bold uppercase tracking-[0.2em] text-blue-700">Account security</p>
            <h1 className="mt-3 text-3xl font-bold tracking-tight text-slate-950">Choose a new password</h1>
            <p className="mt-2 text-sm leading-6 text-slate-600">Use at least eight characters and choose something you do not use on another website.</p>
          </div>

          {!token ? <p role="alert" className="mt-7 rounded-lg border border-red-200 bg-red-50 p-4 text-sm font-semibold text-red-700">This reset link is incomplete. Request a new link from the password recovery page.</p> : null}
          {feedback ? (
            <div role="status" className={`mt-7 flex items-start gap-3 rounded-lg border px-4 py-3 text-sm font-semibold ${completed ? "border-emerald-200 bg-emerald-50 text-emerald-800" : "border-red-200 bg-red-50 text-red-700"}`}>
              {completed ? <CheckCircle2 className="mt-0.5 size-5 shrink-0" aria-hidden="true" /> : null}
              <span>{feedback.text}</span>
            </div>
          ) : null}

          {!completed ? <div className="mt-8 space-y-5">
            <label className="block">
              <span className="text-sm font-semibold text-slate-700">New password</span>
              <span className="relative mt-2 block">
                <LockKeyhole className="pointer-events-none absolute left-4 top-1/2 size-5 -translate-y-1/2 text-slate-400" />
                <input name="password" required minLength={8} type={showPassword ? "text" : "password"} autoComplete="new-password" className="h-12 w-full rounded-lg border border-slate-200 pl-12 pr-12 outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
                <button type="button" onClick={() => setShowPassword((current) => !current)} aria-label={showPassword ? "Hide new password" : "Show new password"} aria-pressed={showPassword} className="absolute right-3 top-1/2 flex size-8 -translate-y-1/2 items-center justify-center rounded-md text-slate-500 hover:bg-slate-100 focus:outline-none focus:ring-2 focus:ring-blue-200">{showPassword ? <EyeOff className="size-5" /> : <Eye className="size-5" />}</button>
              </span>
            </label>
            <label className="block">
              <span className="text-sm font-semibold text-slate-700">Confirm new password</span>
              <span className="relative mt-2 block">
                <LockKeyhole className="pointer-events-none absolute left-4 top-1/2 size-5 -translate-y-1/2 text-slate-400" />
                <input name="confirmation" required minLength={8} type={showConfirmation ? "text" : "password"} autoComplete="new-password" className="h-12 w-full rounded-lg border border-slate-200 pl-12 pr-12 outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
                <button type="button" onClick={() => setShowConfirmation((current) => !current)} aria-label={showConfirmation ? "Hide password confirmation" : "Show password confirmation"} aria-pressed={showConfirmation} className="absolute right-3 top-1/2 flex size-8 -translate-y-1/2 items-center justify-center rounded-md text-slate-500 hover:bg-slate-100 focus:outline-none focus:ring-2 focus:ring-blue-200">{showConfirmation ? <EyeOff className="size-5" /> : <Eye className="size-5" />}</button>
              </span>
            </label>
            <Button type="submit" disabled={busy || !token} className="h-12 w-full bg-blue-600 hover:bg-blue-700">{busy ? "Updating password..." : "Set new password"}</Button>
          </div> : null}

          <Link to="/login" className="mt-6 block text-center text-sm font-semibold text-blue-700 hover:text-blue-800">{completed ? "Sign in with your new password" : "Return to login"}</Link>
        </form>
      </Container>
    </section>
  );
}
