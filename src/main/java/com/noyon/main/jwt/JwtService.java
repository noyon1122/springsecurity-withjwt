package com.noyon.main.jwt;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.noyon.main.model.User;
import com.noyon.main.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Autowired
	private TokenRepository tokenRepository;
	private final String SECRET_KEY="dassdkjfsdjierwkjdfdskdfsjklnoyondsjksdkfhk"; //generated it from web site 
	
	//extract allClaims from token
	
	private Claims extractAllClaims(String token)
	{
		return Jwts
				.parser()
				.verifyWith(getSigninKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
	//generated Token
	
	public String generatedToken(User user)
	{
		return Jwts
			   .builder()
			   .subject(user.getUsername())
			   .claim("role", user.getRole())
			   .issuedAt(new Date(System.currentTimeMillis()))
			   .expiration(new Date(System.currentTimeMillis()+24+60*60*1000))
			   .signWith(getSigninKey())
			   .compact();
	}
	
	public SecretKey getSigninKey()
	{
		byte [] keyBytes=Decoders.BASE64URL.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	//extract user name form token
	
	public String extractUsername(String token)
	{
		return extractClaim(token, Claims::getSubject);
	}
	
	//check the token is expired not not
	
	public boolean isTokenExpired(String token)
	{
		return extractExpiration(token).before(new Date());
	}
	
	private Date extractExpiration(String token)
	{
		return extractClaim(token,Claims::getExpiration);
	}
	
	//extract claim form all extract claim
	public <T> T extractClaim(String token,Function<Claims, T> resolver)
	{
		Claims claims=extractAllClaims(token);
		return resolver.apply(claims);
	}
	
	//check the token is valid or not
	public boolean isValid(String token,UserDetails userdetails)
	{
		String username=extractUsername(token);
		
       boolean validToken=tokenRepository.findByToken(token).map(t -> !t.isLogout()).orElse(false);
       
       return (username.contains(userdetails.getUsername()) && !isTokenExpired(token) && validToken);
	}
	
	//extract user role 
	public String extractUserrole(String token)
	{
		return extractClaim(token, claims->claims.get("role",String.class));
	}
	
	
}
