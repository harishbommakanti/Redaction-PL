public class Token
{
    public TokenType name = null;
    public String content = "";

    public Token(TokenType name, String content)
    {
        this.name = name;
        this.content = content;
    }
}