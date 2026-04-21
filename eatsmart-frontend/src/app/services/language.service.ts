import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { translations } from '../shared/i18n';

@Injectable({
  providedIn: 'root'
})
export class LanguageService {
  private currentLanguage = new BehaviorSubject<'es' | 'en'>('es');
  public currentLanguage$ = this.currentLanguage.asObservable();

  constructor() {
    const savedLanguage = localStorage.getItem('language') as 'es' | 'en' || 'es';
    this.currentLanguage.next(savedLanguage);
  }

  setLanguage(lang: 'es' | 'en') {
    this.currentLanguage.next(lang);
    localStorage.setItem('language', lang);
  }

  getTranslation(key: keyof typeof translations.es): string {
    const lang = this.currentLanguage.value;
    return translations[lang][key] || translations.en[key];
  }

  getCurrentLanguage(): 'es' | 'en' {
    return this.currentLanguage.value;
  }
}
