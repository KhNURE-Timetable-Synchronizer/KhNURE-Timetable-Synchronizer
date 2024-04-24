import Cookies from 'js-cookie';

export function isAuthenticated() {
  const authCookie = Cookies.get('auth')

  return !!authCookie
}