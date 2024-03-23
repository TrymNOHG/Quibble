describe("Tests for registration", () => {
    const base_url_site = "http://localhost5173/"
    const base_url_endpoint = "http://localhost8080/api/user"

    beforeEach(() => {
        cy.intercept("POST", `${base_url_endpoint}/user/register`, {
            statusCode: 200,
            body: {
                token: "e2e-token"
            }
        }).as("registerRequest");

        cy.intercept("GET", `${base_url_endpoint}/user/getUser`,  {
            statusCode: 200,
            body: {
                userID:  "123123",
                email:  "test@ntnu.stud.no",
                username: "test123"
            }
        }).as("getUserInfo");

        cy.visit(`${base_url_site}`)
        cy.contains('a', 'Logg inn').click()
    })

    it('should be possible to find the different input fields', () => {
        cy.get('.input-fields').should('be.visible')

        cy.get('input[name="email"').should('be.visible')
        cy.get('input[name="username"').should('be.visible')
        cy.get('input[name="password"').should('be.visible')
        cy.get('input[name="conf_password"').should('be.visible')

    })

    it('Checking for error handling (missing username)', () => {
        cy.get('.input-fields').should('be.visible')

        cy.get('input[name="email"').should('be.visible')
        cy.get('input[name="password"').should('be.visible')
        cy.get('input[name="conf_password"').should('be.visible')

        cy.get('button[type="submit"]').click()
        cy.get('.has-errors').should('exist')
    })

    it('Checking for error handling (missing email)', () => {
        cy.get('.input-fields').should('be.visible')

        cy.get('input[name="username"').should('be.visible')
        cy.get('input[name="password"').should('be.visible')
        cy.get('input[name="conf_password"').should('be.visible')

        cy.get('button[type="submit"]').click()
        cy.get('.has-errors').should('exist')
    })

    it('Checking for error handling (missing password)', () => {
        cy.get('.input-fields').should('be.visible')

        cy.get('input[name="email"').should('be.visible')
        cy.get('input[name="username"').should('be.visible')
        cy.get('input[name="conf_password"').should('be.visible')

        cy.get('button[type="submit"]').click()
        cy.get('.has-errors').should('exist')
    })

    it('Checking for error handling (missing confirm password)', () => {
        cy.get('.input-fields').should('be.visible')

        cy.get('input[name="email"').should('be.visible')
        cy.get('input[name="username"').should('be.visible')
        cy.get('input[name="password"').should('be.visible')

        cy.get('button[type="submit"]').click()
        cy.get('.has-errors').should('exist')
    })

    it('test error when conf password doesnt match', () => {
        cy.get('.input-fields').should('be.visible')

        cy.get('input[name="email"').type("test@ntnu.stud.no")
        cy.get('input[name="username"]').type("test123")
        cy.get('input[name="password"]').type("12345678")
        cy.get('input[name="conf_passowrd"]').type("123321123")

        cy.get('button[type="submit"]').click()
        cy.get('.has-errors').should('exist')
    })


    it('should be possible login on user', () => {
        cy.get('.input-fields').should('be.visible')

        cy.get('input[name="email"').type("test@ntnu.stud.no")
        cy.get('input[name="username"]').type("test123")
        cy.get('input[name="password"]').type("12345678")
        cy.get('input[name="conf_passowrd"]').type("12345678")

        cy.get('button[type="submit"]').click()

        cy.wait('@registerRequest', {timeout: 10000})
        cy.wait('@getUserInfo', {timeout: 10000})
        cy.url().should('include', '/home')
    })
})