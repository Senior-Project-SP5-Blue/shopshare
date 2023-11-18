import React, {
  PropsWithChildren,
  createContext,
  useContext,
  useState,
} from 'react';
import SignInPayload from '../models/auth/SignInPayload';
import UserDto from '../models/user/UserDto';
import SignInRequest from '../models/auth/SignInRequest';
import SignUpRequest from '../models/auth/SignUpRequest';

const AuthContext = createContext<any>(undefined);

const useProvideAuth = () => {
  const [user, setUser] = useState<UserDto | null>();
  const [accessToken, setAccessToken] = useState<string | null>();
  const [refreshToken, setRefreshToken] = useState<string | null>();
  const [errors, setErrors] = useState<any[]>();
  const [isLoading, setIsLoading] = useState<boolean>();

  const signIn = (request: SignInRequest) => {
    setIsLoading(true);
    fetch(`localhost:8080/api/v1/auth/signin`, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(request),
    }).then(res => {
      setIsLoading(false);
      if (!res.ok) {
        res.json().then(err => {
          setErrors(err);
          return;
        });
      }
      res.json().then((r: SignInPayload) => {
        setUser(r.userContext);
        setAccessToken(r.accessToken);
        setRefreshToken(r.refreshToken);
      });
    });
  };
  const signUp = (request: SignUpRequest) => {
    setIsLoading(true);
    fetch(`localhost:8080/api/v1/auth/signup`, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(request),
    }).then(res => {
      setIsLoading(false);
      if (!res.ok) {
        res.json().then(err => {
          setErrors(err);
          return;
        });
      }
    });
  };

  const signOut = () => {
    fetch(`localhost:8080/api/v1/auth`, {
      method: 'POST',
    }).then(res => {
      if (res.ok) {
        setUser(null);
        setAccessToken(null);
        setRefreshToken(null);
      }
      res.json().then((r: SignInPayload) => {
        setUser(r.userContext);
        setAccessToken(r.accessToken);
        setRefreshToken(r.refreshToken);
      });
    });
  };

  return {
    user,
    signUp,
    signIn,
    signOut,
    accessToken,
    refreshToken,
    errors,
    isLoading,
  };
};

export const AuthProvider: React.FunctionComponent<PropsWithChildren> = ({
  children,
}) => {
  const [auth, setAuth] = useState<SignInPayload | undefined>();
  const securityContext = useProvideAuth();

  return (
    <AuthContext.Provider value={{auth, setAuth}}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);

// export default AuthContext;
