interface GoogleCredentialResponse {
  credential: string;
  select_by: string;
}

interface GoogleAccountsIdApi {
  initialize(options: {
    client_id: string;
    callback: (response: GoogleCredentialResponse) => void;
    ux_mode?: "popup" | "redirect";
  }): void;
  renderButton(
    element: HTMLElement,
    options: {
      type?: "standard" | "icon";
      theme?: "outline" | "filled_blue" | "filled_black";
      size?: "large" | "medium" | "small";
      text?: "signin_with" | "signup_with" | "continue_with" | "signin";
      shape?: "rectangular" | "pill" | "circle" | "square";
      logo_alignment?: "left" | "center";
      width?: number;
    },
  ): void;
  cancel(): void;
}

interface Window {
  google?: {
    accounts: {
      id: GoogleAccountsIdApi;
    };
  };
}
