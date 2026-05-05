import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { BehaviorSubject } from 'rxjs';
import { translations } from '../shared/i18n';

@Injectable({
  providedIn: 'root'
})
export class LanguageService {
  private currentLanguage = new BehaviorSubject<'es' | 'en'>('es');
  public currentLanguage$ = this.currentLanguage.asObservable();

  // true si estamos en el navegador, false si estamos en SSR (servidor)
  private isBrowser: boolean;

  constructor(@Inject(PLATFORM_ID) platformId: Object) {
    this.isBrowser = isPlatformBrowser(platformId);

    // Cargar idioma guardado solo en navegador
    if (this.isBrowser) {
      const savedLanguage = (localStorage.getItem('language') as 'es' | 'en') || 'es';
      this.currentLanguage.next(savedLanguage);
    }
  }

  setLanguage(lang: 'es' | 'en') {
    this.currentLanguage.next(lang);

    // Guardar en localStorage solo en navegador
    if (this.isBrowser) {
      localStorage.setItem('language', lang);
    }
  }

  getTranslation(key: keyof typeof translations.es): string {
    const lang = this.currentLanguage.value;
    return translations[lang][key] || translations.en[key];
  }

  getCurrentLanguage(): 'es' | 'en' {
    return this.currentLanguage.value;
  }
}
