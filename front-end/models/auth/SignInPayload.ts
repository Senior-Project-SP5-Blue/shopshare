import UserDto from '../user/UserDto';

type SignInPayload = {
  userContext: UserDto;
  accessToken: string;
  refreshToken: string;
};

export default SignInPayload;
