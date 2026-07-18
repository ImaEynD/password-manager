import axios from 'axios';

export interface PasswordEntry {
  service: string;
  login: string;
  password: string;
}

export interface AuthResponse {
  token: string;
}

const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' }
});

api.interceptors.response.use(
  (res) => res,
  (err) => {
    if (err.response?.status === 401) {
      window.dispatchEvent(new CustomEvent('session-expired'));
    }
    return Promise.reject(err);
  }
);

// === АВТОРИЗАЦИЯ ===
export const register = (username: string, password: string, masterKey: string) =>
  api.post<AuthResponse>('/auth/register', { username, password, masterKey });

export const login = (username: string, password: string) =>
  api.post<AuthResponse>('/auth/login', { username, password });

// === РАЗБЛОКИРОВКА ХРАНИЛИЩА ===
export const unlockVault = (token: string, masterKey: string) =>
  api.post('/passwords/unlock', { masterKey }, {
    headers: { 'Authorization': `Bearer ${token}` }
  });

// === ХРАНИЛИЩЕ ===
export const getPasswords = (token: string) =>
  api.get<PasswordEntry[]>('/passwords', {
    headers: { 
      'Authorization': `Bearer ${token}`,
      'X-Session-Key': token
    }
  });

export const addPassword = (token: string, entry: Omit<PasswordEntry, 'id'>) =>
  api.post('/passwords', entry, {
    headers: { 
      'Authorization': `Bearer ${token}`,
      'X-Session-Key': token
    }
  });

export const updatePassword = (token: string, service: string, entry: Omit<PasswordEntry, 'id'>) =>
  api.put(`/passwords/${service}`, entry, {
    headers: { 
      'Authorization': `Bearer ${token}`,
      'X-Session-Key': token
    }
  });

export const deletePassword = (token: string, service: string) =>
  api.delete(`/passwords/${service}`, {
    headers: { 
      'Authorization': `Bearer ${token}`,
      'X-Session-Key': token
    }
  });