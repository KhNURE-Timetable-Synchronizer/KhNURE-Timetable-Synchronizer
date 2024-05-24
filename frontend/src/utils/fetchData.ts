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
    throw new Error(`${name} response was not ok`)
  }
  const data = await res.json()
  return data as T
}
