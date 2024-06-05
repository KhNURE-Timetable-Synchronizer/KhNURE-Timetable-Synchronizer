import { ErrorWithResponse } from "../errors/ErrorWithResponse"
export async function fetchAndHandleData<T>(
  name: string,
  onUnauthorized: () => void,
  ...args: Parameters<typeof fetch>
) {
  const res = await fetch(args[0], {
    credentials: "include",
    ...args[1],
  })
  if (!res.ok) {
    if (res.status === 401) {
      console.warn(`401 on ${name}, logging out`)
      onUnauthorized()
      return
    }
    if (res.status === 404) {
      console.warn(`404 on ${name}`)
      return null
    }
    if (res.status >= 400 && res.status < 500) {
      const err: { message: string } = await res.json()
      alert(err.message)
    }

    throw new ErrorWithResponse(`${name} response was not ok`, res)
  }
  const resText = await res.text()
  const data = resText && JSON.parse(resText)
  return data as T
}
