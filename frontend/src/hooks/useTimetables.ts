import { useQuery } from "@tanstack/react-query"

export type PersonalTimetable = { id: number; name: string }

export const personalTimetablesQueryKey = ["personalTimetables"]
const fetchPersonalTimetables = async () => {
  const res = await fetch(
    `${import.meta.env.VITE_BACKEND_URL}/api/v1/timetables/google`,
    {
      credentials: "include",
    }
  )
  if (!res.ok) {
    throw new Error("GET Personal Timetables response was not ok")
  }
  const data = await res.json()
  return data as PersonalTimetable[]
}

export type Timetable = { id: number; name: string; type: string }

export const allTimetablesQueryKey = ["allTimetables"]
const fetchAllTimetables = async () => {
  const res = await fetch(
    `${import.meta.env.VITE_BACKEND_URL}/api/v1/khnure/timetables`,
    {
      credentials: "include",
    }
  )
  if (!res.ok) {
    throw new Error("GET All Timetables response was not ok")
  }
  const data = await res.json()
  return data as Timetable[]
}

export default function useTimetables() {
  const personal = useQuery({
    queryKey: personalTimetablesQueryKey,
    queryFn: fetchPersonalTimetables,
  })

  const all = useQuery({
    queryKey: allTimetablesQueryKey,
    queryFn: fetchAllTimetables,
    refetchOnWindowFocus: false,
  })

  const addTimetable = async (params: {
    timetableId: number
    timetableType: string
    startDate: Date
    endDate: Date
  }) => {
    const res = await fetch(
      `${import.meta.env.VITE_BACKEND_URL}/api/v1/timetables/google`,
      {
        method: "POST",
        body: JSON.stringify({ ...params }),
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
      }
    )
    if (!res.ok) {
      throw new Error("POST Timetable response was not ok")
    }
  }

  return {
    personalTimetables: personal,
    allTimetables: all,
    addTimetable,
  }
}
