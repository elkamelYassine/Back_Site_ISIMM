import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './fichier-admin.reducer';

export const FichierAdminDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const fichierAdminEntity = useAppSelector(state => state.fichierAdmin.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="fichierAdminDetailsHeading">
          <Translate contentKey="isimmManagerApp.fichierAdmin.detail.title">FichierAdmin</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{fichierAdminEntity.id}</dd>
          <dt>
            <span id="titre">
              <Translate contentKey="isimmManagerApp.fichierAdmin.titre">Titre</Translate>
            </span>
          </dt>
          <dd>{fichierAdminEntity.titre}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="isimmManagerApp.fichierAdmin.type">Type</Translate>
            </span>
          </dt>
          <dd>{fichierAdminEntity.type}</dd>
          <dt>
            <span id="file">
              <Translate contentKey="isimmManagerApp.fichierAdmin.file">File</Translate>
            </span>
          </dt>
          <dd>
            {fichierAdminEntity.file ? (
              <div>
                {fichierAdminEntity.fileContentType ? (
                  <a onClick={openFile(fichierAdminEntity.fileContentType, fichierAdminEntity.file)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {fichierAdminEntity.fileContentType}, {byteSize(fichierAdminEntity.file)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="demandeValide">
              <Translate contentKey="isimmManagerApp.fichierAdmin.demandeValide">Demande Valide</Translate>
            </span>
          </dt>
          <dd>{fichierAdminEntity.demandeValide ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="isimmManagerApp.fichierAdmin.etudiant">Etudiant</Translate>
          </dt>
          <dd>{fichierAdminEntity.etudiant ? fichierAdminEntity.etudiant.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/fichier-admin" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/fichier-admin/${fichierAdminEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FichierAdminDetail;
