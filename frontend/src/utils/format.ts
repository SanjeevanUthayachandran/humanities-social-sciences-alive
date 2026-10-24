const dateFormatter = new Intl.DateTimeFormat("en", {
  month: "short",
  day: "numeric",
  year: "numeric",
});

const monthFormatter = new Intl.DateTimeFormat("en", {
  month: "long",
  year: "numeric",
});

export function formatDate(value: string) {
  if (!value) return "Date pending";
  return dateFormatter.format(new Date(value));
}

export function formatMonth(value: string) {
  if (!value) return "Schedule pending";
  return monthFormatter.format(new Date(value));
}

export function uniqueValues(values: string[]) {
  return Array.from(new Set(values)).sort((a, b) => a.localeCompare(b));
}
