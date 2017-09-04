
import User from "./User"

export default interface Team {
    id: string,
    name: string,
    members: User[]
}