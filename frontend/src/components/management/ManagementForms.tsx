import { BookPlus, CalendarPlus, CreditCard, Eye, EyeOff, ImagePlus, KeyRound, UserPlus } from "lucide-react";
import { useState, type FormEvent } from "react";
import { Button } from "@/components/ui/button";
import { changePassword } from "@/services/authService";
import {
  assignInstructor,
  createCourse,
  createProgram,
  mediaDownloadUrl,
  uploadCardImage,
  updateCoursePaymentSettings,
  type CourseCategory,
  type ManagementCourse,
} from "@/services/managementApi";
import { getRequestErrorMessage } from "@/services/requestError";

const inputClass = "mt-2 h-11 w-full rounded-lg border border-slate-200 bg-white px-3 text-sm outline-none transition focus:border-blue-500 focus:ring-3 focus:ring-blue-100";
const textareaClass = "mt-2 min-h-28 w-full rounded-lg border border-slate-200 bg-white p-3 text-sm outline-none transition focus:border-blue-500 focus:ring-3 focus:ring-blue-100";
const cardClass = "rounded-2xl border border-slate-200 bg-white p-6 shadow-sm";

const MAX_CARD_IMAGE_BYTES = 2 * 1024 * 1024;

function Feedback({ value }: { value: { type: "success" | "error"; text: string } | null }) {
  if (!value) return null;
  return <p role="status" className={`mt-4 rounded-lg px-4 py-3 text-sm font-semibold ${value.type === "success" ? "bg-emerald-50 text-emerald-800" : "bg-red-50 text-red-700"}`}>{value.text}</p>;
}

function CardImageInput() {
  return <label className="sm:col-span-2">
    <span className="flex items-center gap-2 text-sm font-semibold text-slate-700"><ImagePlus className="size-4 text-blue-600" />Card image</span>
    <input name="cardImage" type="file" accept="image/jpeg,image/png,image/webp" required className={`${inputClass} h-auto py-2.5 file:mr-4 file:rounded-md file:border-0 file:bg-blue-50 file:px-3 file:py-2 file:font-semibold file:text-blue-700`} />
    <span className="mt-2 block text-xs leading-5 text-slate-500">Recommended: 1600 × 900 px (16:9). JPG, PNG, or WebP. Maximum file size: 2 MB. Keep important text away from the edges.</span>
  </label>;
}

async function uploadImageFrom(form: FormData, title: string) {
  const file = form.get("cardImage");
  if (!(file instanceof File) || file.size === 0) throw new Error("Choose a card image before saving.");
  if (file.size > MAX_CARD_IMAGE_BYTES) throw new Error("The card image must be 2 MB or smaller.");
  const media = await uploadCardImage(file, `${title} card image`);
  return { mediaId: media.id, imageUrl: mediaDownloadUrl(media.id) };
}

export function CourseForm({ categories, onCreated }: { categories: CourseCategory[]; onCreated: (course: ManagementCourse) => void }) {
  const [busy, setBusy] = useState(false);
  const [feedback, setFeedback] = useState<{ type: "success" | "error"; text: string } | null>(null);
  const [isFree, setIsFree] = useState(false);

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setBusy(true);
    setFeedback(null);
    const form = new FormData(event.currentTarget);
    try {
      const title = String(form.get("title"));
      const image = await uploadImageFrom(form, title);
      const course = await createCourse({
        categoryId: String(form.get("categoryId")),
        title,
        subtitle: String(form.get("subtitle")),
        description: String(form.get("description")),
        level: String(form.get("level")),
        visibility: "PUBLIC",
        isFree,
        price: isFree ? 0 : Number(form.get("price") || 0),
        discountedPrice: isFree || !form.get("discountedPrice") ? undefined : Number(form.get("discountedPrice")),
        currency: "LKR",
        cardImageUrl: image.imageUrl,
        certificationAvailable: true,
        tags: String(form.get("tags")).split(",").map((tag) => tag.trim()).filter(Boolean),
      });
      onCreated(course);
      event.currentTarget.reset();
      setIsFree(false);
      setFeedback({ type: "success", text: "Course created as a draft. Review it, then publish when ready." });
    } catch (error) {
      setFeedback({ type: "error", text: getRequestErrorMessage(error, "The course could not be created.") });
    } finally {
      setBusy(false);
    }
  }

  return <form onSubmit={submit} className={cardClass}>
    <div className="flex items-center gap-3"><span className="rounded-xl bg-blue-50 p-2.5 text-blue-700"><BookPlus className="size-5" /></span><div><h2 className="font-bold text-slate-950">Create a course</h2><p className="text-sm text-slate-500">New courses appear in the public catalogue.</p></div></div>
    <div className="mt-6 grid gap-4 sm:grid-cols-2">
      <label className="sm:col-span-2"><span className="text-sm font-semibold text-slate-700">Course title</span><input name="title" required className={inputClass} /></label>
      <label><span className="text-sm font-semibold text-slate-700">Category</span><select name="categoryId" required className={inputClass}><option value="">{categories.length ? "Select category" : "No categories available"}</option>{categories.map((item) => <option key={item.id} value={item.id}>{item.name}</option>)}</select></label>
      <label><span className="text-sm font-semibold text-slate-700">Level</span><select name="level" className={inputClass}><option>BEGINNER</option><option>INTERMEDIATE</option><option>ADVANCED</option><option>ALL_LEVELS</option></select></label>
      <label className="sm:col-span-2"><span className="text-sm font-semibold text-slate-700">Short promise</span><input name="subtitle" required className={inputClass} placeholder="What will learners gain?" /></label>
      <label className="sm:col-span-2"><span className="text-sm font-semibold text-slate-700">Description</span><textarea name="description" required className={textareaClass} /></label>
      <CardImageInput />
      <label className="sm:col-span-2"><span className="text-sm font-semibold text-slate-700">Search tags</span><input name="tags" className={inputClass} placeholder="writing, research, ethics" /></label>
      <label className="flex items-center gap-3 rounded-lg border border-slate-200 p-3 text-sm font-semibold text-slate-700"><input type="checkbox" checked={isFree} onChange={(event) => setIsFree(event.target.checked)} className="size-4" />Free course</label>
      {!isFree ? <div className="grid grid-cols-2 gap-3"><label><span className="text-xs font-semibold text-slate-600">Price (LKR)</span><input name="price" type="number" min="0" required className={inputClass} /></label><label><span className="text-xs font-semibold text-slate-600">Offer price</span><input name="discountedPrice" type="number" min="0" className={inputClass} /></label></div> : <div />}
    </div>
    <Feedback value={feedback} />
    <Button disabled={busy || categories.length === 0} className="mt-5 bg-blue-600 hover:bg-blue-700">{busy ? "Saving..." : "Create draft course"}</Button>
  </form>;
}

export function ProgramForm() {
  const [busy, setBusy] = useState(false);
  const [feedback, setFeedback] = useState<{ type: "success" | "error"; text: string } | null>(null);
  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setBusy(true);
    const form = new FormData(event.currentTarget);
    const type = String(form.get("recordType")) as "workshops" | "events";
    try {
      const title = String(form.get("title"));
      const image = await uploadImageFrom(form, title);
      await createProgram(type, {
        title, description: String(form.get("description")),
        eventType: type === "workshops" ? "WORKSHOP" : String(form.get("eventType")),
        startTime: String(form.get("startTime")), endTime: String(form.get("endTime")),
        locationType: String(form.get("locationType")), locationDetails: String(form.get("locationDetails")),
        timezone: "Asia/Colombo", maxParticipants: Number(form.get("maxParticipants") || 0),
        isFree: true, price: 0, coverMediaId: image.mediaId,
      });
      event.currentTarget.reset();
      setFeedback({ type: "success", text: "Program created. Publish it after reviewing the schedule." });
    } catch (error) {
      setFeedback({ type: "error", text: getRequestErrorMessage(error, "The program could not be created.") });
    } finally { setBusy(false); }
  }
  return <form onSubmit={submit} className={cardClass}>
    <div className="flex items-center gap-3"><span className="rounded-xl bg-emerald-50 p-2.5 text-emerald-700"><CalendarPlus className="size-5" /></span><div><h2 className="font-bold text-slate-950">Create a live program</h2><p className="text-sm text-slate-500">Add a workshop, seminar, or public event.</p></div></div>
    <div className="mt-6 grid gap-4 sm:grid-cols-2">
      <label><span className="text-sm font-semibold text-slate-700">Record type</span><select name="recordType" className={inputClass}><option value="workshops">Workshop</option><option value="events">Event</option></select></label>
      <label><span className="text-sm font-semibold text-slate-700">Event format</span><select name="eventType" className={inputClass}><option>EVENT</option><option>SEMINAR</option><option>WEBINAR</option><option>CONFERENCE</option></select></label>
      <label className="sm:col-span-2"><span className="text-sm font-semibold text-slate-700">Title</span><input name="title" required className={inputClass} /></label>
      <label className="sm:col-span-2"><span className="text-sm font-semibold text-slate-700">Description</span><textarea name="description" required className={textareaClass} /></label>
      <CardImageInput />
      <label><span className="text-sm font-semibold text-slate-700">Starts</span><input name="startTime" type="datetime-local" required className={inputClass} /></label>
      <label><span className="text-sm font-semibold text-slate-700">Ends</span><input name="endTime" type="datetime-local" required className={inputClass} /></label>
      <label><span className="text-sm font-semibold text-slate-700">Location type</span><select name="locationType" className={inputClass}><option>ONLINE</option><option>PHYSICAL</option><option>HYBRID</option></select></label>
      <label><span className="text-sm font-semibold text-slate-700">Capacity</span><input name="maxParticipants" type="number" min="1" defaultValue="40" className={inputClass} /></label>
      <label className="sm:col-span-2"><span className="text-sm font-semibold text-slate-700">Location or joining note</span><input name="locationDetails" required className={inputClass} /></label>
    </div>
    <Feedback value={feedback} /><Button disabled={busy} className="mt-5 bg-emerald-600 hover:bg-emerald-700">{busy ? "Saving..." : "Create program"}</Button>
  </form>;
}

export function InstructorAssignmentForm({ courses }: { courses: ManagementCourse[] }) {
  const [busy, setBusy] = useState(false);
  const [feedback, setFeedback] = useState<{ type: "success" | "error"; text: string } | null>(null);
  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault(); setBusy(true); const form = new FormData(event.currentTarget);
    try { setFeedback({ type: "success", text: await assignInstructor(String(form.get("email")), String(form.get("courseId")), form.get("primary") === "on") }); event.currentTarget.reset(); }
    catch (error) { setFeedback({ type: "error", text: getRequestErrorMessage(error, "Instructor assignment failed.") }); }
    finally { setBusy(false); }
  }
  return <form onSubmit={submit} className={cardClass}>
    <div className="flex items-center gap-3"><span className="rounded-xl bg-violet-50 p-2.5 text-violet-700"><UserPlus className="size-5" /></span><div><h2 className="font-bold text-slate-950">Assign an instructor</h2><p className="text-sm text-slate-500">The email must belong to an existing HSSA account.</p></div></div>
    <label className="mt-6 block"><span className="text-sm font-semibold text-slate-700">Registered email</span><input name="email" type="email" required className={inputClass} /></label>
    <label className="mt-4 block"><span className="text-sm font-semibold text-slate-700">Course</span><select name="courseId" required className={inputClass}><option value="">{courses.length ? "Select course" : "No courses available"}</option>{courses.map((course) => <option key={course.id} value={course.id}>{course.title}</option>)}</select></label>
    <label className="mt-4 flex items-center gap-3 text-sm font-semibold text-slate-700"><input name="primary" type="checkbox" className="size-4" />Primary instructor</label>
    <Feedback value={feedback} /><Button disabled={busy || courses.length === 0} className="mt-5 bg-violet-600 hover:bg-violet-700">{busy ? "Assigning..." : "Grant course access"}</Button>
  </form>;
}

export function PaymentSettingsForm({ courses }: { courses: ManagementCourse[] }) {
  const [busy, setBusy] = useState(false);
  const [feedback, setFeedback] = useState<{ type: "success" | "error"; text: string } | null>(null);
  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault(); setBusy(true); const form = new FormData(event.currentTarget);
    try { await updateCoursePaymentSettings(String(form.get("courseId")), { gatewayCode: String(form.get("gatewayCode")), onlinePaymentEnabled: form.get("online") === "on", bankTransferEnabled: form.get("bank") === "on", installmentsEnabled: form.get("installments") === "on" }); setFeedback({ type: "success", text: "Payment policy updated for this course." }); }
    catch (error) { setFeedback({ type: "error", text: getRequestErrorMessage(error, "Payment settings could not be updated.") }); }
    finally { setBusy(false); }
  }
  return <form onSubmit={submit} className={cardClass}>
    <div className="flex items-center gap-3"><span className="rounded-xl bg-amber-50 p-2.5 text-amber-700"><CreditCard className="size-5" /></span><div><h2 className="font-bold text-slate-950">Course payment policy</h2><p className="text-sm text-slate-500">Administrator-only. Credentials stay in server environment variables.</p></div></div>
    <label className="mt-6 block"><span className="text-sm font-semibold text-slate-700">Course</span><select name="courseId" required className={inputClass}><option value="">{courses.length ? "Select course" : "No courses available"}</option>{courses.map((course) => <option key={course.id} value={course.id}>{course.title}</option>)}</select></label>
    <label className="mt-4 block"><span className="text-sm font-semibold text-slate-700">Gateway</span><select name="gatewayCode" className={inputClass}><option>PAYHERE</option></select></label>
    <div className="mt-5 grid gap-3 text-sm font-semibold text-slate-700 sm:grid-cols-3">{[["online", "Online card payments"], ["bank", "Bank transfer"], ["installments", "Installments"]].map(([name, label]) => <label key={name} className="flex items-center gap-2 rounded-lg border border-slate-200 p-3"><input name={name} type="checkbox" defaultChecked={name === "online"} className="size-4" />{label}</label>)}</div>
    <Feedback value={feedback} /><Button disabled={busy || courses.length === 0} className="mt-5 bg-amber-600 hover:bg-amber-700">{busy ? "Saving..." : "Save payment policy"}</Button>
  </form>;
}

export function PasswordForm() {
  const [busy, setBusy] = useState(false);
  const [showPasswords, setShowPasswords] = useState(false);
  const [feedback, setFeedback] = useState<{ type: "success" | "error"; text: string } | null>(null);
  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault(); const form = new FormData(event.currentTarget); const next = String(form.get("newPassword"));
    if (next !== String(form.get("confirmation"))) { setFeedback({ type: "error", text: "The new passwords do not match." }); return; }
    setBusy(true); try { setFeedback({ type: "success", text: await changePassword(String(form.get("currentPassword")), next) }); event.currentTarget.reset(); }
    catch (error) { setFeedback({ type: "error", text: getRequestErrorMessage(error, "Password could not be changed.") }); }
    finally { setBusy(false); }
  }
  return <form onSubmit={submit} className={cardClass}>
    <div className="flex items-center gap-3"><span className="rounded-xl bg-slate-100 p-2.5 text-slate-700"><KeyRound className="size-5" /></span><div><h2 className="font-bold text-slate-950">Change password</h2><p className="text-sm text-slate-500">All other refresh sessions are revoked after a change.</p></div></div>
    <div className="mt-6 grid gap-4 sm:grid-cols-3"><label><span className="text-sm font-semibold text-slate-700">Current password</span><input name="currentPassword" type={showPasswords ? "text" : "password"} required className={inputClass} /></label><label><span className="text-sm font-semibold text-slate-700">New password</span><input name="newPassword" type={showPasswords ? "text" : "password"} minLength={8} required className={inputClass} /></label><label><span className="text-sm font-semibold text-slate-700">Confirm password</span><input name="confirmation" type={showPasswords ? "text" : "password"} minLength={8} required className={inputClass} /></label></div>
    <button type="button" onClick={() => setShowPasswords((value) => !value)} className="mt-4 inline-flex items-center gap-2 text-sm font-semibold text-slate-600 hover:text-slate-950">{showPasswords ? <EyeOff className="size-4" /> : <Eye className="size-4" />}{showPasswords ? "Hide passwords" : "Show passwords"}</button>
    <Feedback value={feedback} /><Button disabled={busy} className="mt-5 bg-slate-900 hover:bg-slate-800">{busy ? "Updating..." : "Change password"}</Button>
  </form>;
}
