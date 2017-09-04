
export default interface JwtTokenClaims {
    sub: string,
    email: string,
    displayName: string
    exp: number
}