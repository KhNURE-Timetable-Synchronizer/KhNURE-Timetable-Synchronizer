import { useQuery } from "@tanstack/react-query"
import { useAuth } from "../utils/AuthProvider"
import { fetchAndHandleData } from "../utils/fetchData"

export type Timetable = { id: number; name: string; type: string }
export type PersonalTimetable = {
  id: number
  name: string
  googleCalendarId: string
  khnureTimetableId: number
}

export const personalTimetablesQueryKey = ["personalTimetables"]
const fetchPersonalTimetables = async (onUnauthorized: () => void) => {
  return fetchAndHandleData<PersonalTimetable[]>(
    "GET Personal Timetables",
    onUnauthorized,
    `${import.meta.env.VITE_BACKEND_URL}/api/v1/timetables/google`
  )
}

export const allTimetablesQueryKey = ["allTimetables"]
const fetchAllTimetables = async (onUnauthorized: () => void) => {
  return fetchAndHandleData<Timetable[]>(
    "GET All Timetables",
    onUnauthorized,
    `${import.meta.env.VITE_BACKEND_URL}/api/v1/khnure/timetables`
  )
}

export default function useTimetables() {
  const { logout } = useAuth()
  const personal = useQuery({
    queryKey: personalTimetablesQueryKey,
    queryFn: () => fetchPersonalTimetables(logout),
  })

  const all = useQuery({
    queryKey: allTimetablesQueryKey,
    queryFn: () => fetchAllTimetables(logout),
    refetchOnWindowFocus: false,
  })

  const addTimetable = async (params: {
    timetableId: number
    timetableType: string
    startDate: Date
    endDate: Date
  }) => {
    await fetchAndHandleData(
      "POST Timetable",
      logout,
      `${import.meta.env.VITE_BACKEND_URL}/api/v1/timetables/google`,
      {
        method: "POST",
        body: JSON.stringify({ ...params }),
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
      }
    ).catch((error) => {
      console.error(error)
      alert("Failed to add timetable")
      throw error
    })
  }

  const deleteTimetable = async (id: number) => {
    await fetchAndHandleData(
      "DELETE Timetable",
      logout,
      `${import.meta.env.VITE_BACKEND_URL}/api/v1/timetables/google/${id}`,
      {
        method: "DELETE",
        credentials: "include",
      }
    ).catch((error) => {
      console.error(error)
      alert("Failed to delete timetable")
      throw error
    })
  }

  return {
    personalTimetables: personal,
    allTimetables: all,
    addTimetable,
    deleteTimetable,
    refetch: personal.refetch,
  }
}
