import { useQuery } from "@tanstack/react-query"

export const timetablesQueryKey = ["timetables"]
export type TimetableShort = { id: number; name: string }

const fetchTimetables = async () => {
  const res = await fetch(
    `${import.meta.env.VITE_BACKEND_URL}/api/v1/timetables/google`
  )
  if (!res.ok) {
    throw new Error("GET Timetables response was not ok")
  }
  const data = await res.json()
  return data as TimetableShort[]
}

export default function useTimetables() {
  const { data, isError, isLoading, status } = useQuery({
    queryKey: timetablesQueryKey,
    queryFn: fetchTimetables,
  })

  return { timetables: data, status, isError, isLoading }
}
