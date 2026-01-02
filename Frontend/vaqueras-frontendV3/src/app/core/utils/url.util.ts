export function normalizeApiUrl(url?: string | null): string {
  if (!url) return '';
  // Si ya es absoluta, déjala
  if (/^https?:\/\//i.test(url)) return url;

  //  backend devuelve /vaqueras-backend/api/...
  // Lo convertimos a /api/... para que lo agarre el proxy de Angular
  if (url.startsWith('/vaqueras-backend/api/')) return url.replace('/vaqueras-backend', '');

  // Si ya viene como /api/... o /assets/..., se queda igual
  return url;
}
